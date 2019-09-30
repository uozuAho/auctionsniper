package aho.uozu.auctionsniper.tests;

import aho.uozu.auctionsniper.SniperSnapshot;
import aho.uozu.auctionsniper.SnipersTableColumn;
import aho.uozu.auctionsniper.SnipersTableModel;
import com.objogate.exception.Defect;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SnipersTableModelTest {
    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();

    private TableModelListener listener = context.mock(TableModelListener.class);
    private final SnipersTableModel tableModel = new SnipersTableModel();

    @Before
    public void attachModelListener() {
        tableModel.addTableModelListener(listener);
    }

    @Test
    public void
    hasEnoughColumns() {
        assertThat(tableModel.getColumnCount(), equalTo(SnipersTableColumn.values().length));
    }

    @Test public void
    setsUpColumnHeadings() {
        for (SnipersTableColumn column: SnipersTableColumn.values()) {
            assertEquals(column.name, tableModel.getColumnName(column.ordinal()));
        }
    }

    @Test public void
    setsSniperValuesInColumns() {
        SniperSnapshot joining = SniperSnapshot.joining("item id");
        SniperSnapshot bidding = joining.bidding(555, 666);

        context.checking(new Expectations() {{
            allowing(listener).tableChanged(with(anyInsertionEvent()));
            oneOf(listener).tableChanged(with(aChangeInRow(0)));
        }});

        tableModel.addSniper(joining);
        tableModel.sniperStateChanged(bidding);

        assertRowMatchesSnapshot(0, bidding);
    }

    @Test public void
    notifiesListenersWhenAddingASniper() {
        SniperSnapshot joining = SniperSnapshot.joining("item123");
        context.checking(new Expectations() { {
            oneOf(listener).tableChanged(with(anInsertionAtRow(0)));
        }});
        assertEquals(0, tableModel.getRowCount());
        tableModel.addSniper(joining);
        assertEquals(1, tableModel.getRowCount());
        assertRowMatchesSnapshot(0, joining);
    }

    @Test public void
    holdsSnipersInAdditionOrder() {
        context.checking(new Expectations() { {
            ignoring(listener);
        }});
        tableModel.addSniper(SniperSnapshot.joining("item 0"));
        tableModel.addSniper(SniperSnapshot.joining("item 1"));

        assertEquals("item 0", cellValue(0, SnipersTableColumn.ITEM_IDENTIFIER));
        assertEquals("item 1", cellValue(1, SnipersTableColumn.ITEM_IDENTIFIER));
    }

    @Test public void
    updatesCorrectRowForSniper() {
        context.checking(new Expectations() { {
            ignoring(listener);
        }});
        tableModel.addSniper(SniperSnapshot.joining("item 0"));
        var joining1 = SniperSnapshot.joining("item 1");
        tableModel.addSniper(joining1);
        tableModel.sniperStateChanged(joining1.winning(123));

        assertEquals("123", cellValue(1, SnipersTableColumn.LAST_PRICE));
    }

    @Test(expected = Defect.class)
    public void
    throwsDefectIfNoExistingSniperForAnUpdate() {
        context.checking(new Expectations() { {
            ignoring(listener);
        }});
        tableModel.addSniper(SniperSnapshot.joining("item 0"));
        tableModel.sniperStateChanged(SniperSnapshot.joining("this doesn't exist"));
    }

    private String cellValue(int i, SnipersTableColumn column) {
        return tableModel.getValueAt(i, column.ordinal()).toString();
    }

    private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
        assertCellEquals(row, SnipersTableColumn.ITEM_IDENTIFIER, snapshot.itemId);
        assertCellEquals(row, SnipersTableColumn.LAST_PRICE, snapshot.lastPrice);
        assertCellEquals(row, SnipersTableColumn.LAST_BID, snapshot.lastBid);
        assertCellEquals(row, SnipersTableColumn.SNIPER_STATE, SnipersTableModel.textFor(snapshot.state));
    }

    private void assertCellEquals(final int row, SnipersTableColumn column, Object expected) {
        final int columnIndex = column.ordinal();
        assertEquals(expected, tableModel.getValueAt(row, columnIndex));
    }

    private Matcher<TableModelEvent> aChangeInRow(int row) {
        return new EventInRowMatcher(row, TableModelEvent.UPDATE);
    }

    private Matcher<TableModelEvent> anInsertionAtRow(int row) {
        return new EventInRowMatcher(row, TableModelEvent.INSERT);
    }

    private Matcher<TableModelEvent> anyInsertionEvent() {
        return new AnyInsertionEvent();
    }

    private class EventInRowMatcher extends TypeSafeMatcher<TableModelEvent> {

        private final int row;
        private int eventType;

        public EventInRowMatcher(int row, int eventType) {
            this.row = row;
            this.eventType = eventType;
        }

        @Override
        protected boolean matchesSafely(TableModelEvent tableModelEvent) {
            var isUpdateType = tableModelEvent.getType() == eventType;
            var isThisRowAffected = tableModelEvent.getFirstRow() <= row && row <= tableModelEvent.getLastRow();
            return isUpdateType && isThisRowAffected;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("event " + eventType + " in row " + row);
        }
    }

    private class AnyInsertionEvent extends TypeSafeMatcher<TableModelEvent> {
        @Override
        protected boolean matchesSafely(TableModelEvent tableModelEvent) {
            return tableModelEvent.getType() == TableModelEvent.INSERT;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("event of type INSERT");
        }
    }
}
