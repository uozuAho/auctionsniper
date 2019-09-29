package aho.uozu.auctionsniper.tests;

import aho.uozu.auctionsniper.SniperSnapshot;
import aho.uozu.auctionsniper.SniperState;
import aho.uozu.auctionsniper.SnipersTableColumn;
import aho.uozu.auctionsniper.SnipersTableModel;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SnipersTableModelTest {
    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();

    private TableModelListener listener = context.mock(TableModelListener.class);
    private final SnipersTableModel model = new SnipersTableModel();

    @Before
    public void attachModelListener() {
        model.addTableModelListener(listener);
    }

    @Test
    public void
    hasEnoughColumns() {
        assertThat(model.getColumnCount(), equalTo(SnipersTableColumn.values().length));
    }

    @Test public void
    setsUpColumnHeadings() {
        for (SnipersTableColumn column: SnipersTableColumn.values()) {
            assertEquals(column.name, model.getColumnName(column.ordinal()));
        }
    }

    @Test public void
    setsSniperValuesInColumns() {
        context.checking(new Expectations() {{
            oneOf(listener).tableChanged(with(aRowChangedEvent()));
        }});

        model.sniperStateChanged(new SniperSnapshot("item id", 555, 666, SniperState.BIDDING));

        assertColumnEquals(SnipersTableColumn.ITEM_IDENTIFIER, "item id");
        assertColumnEquals(SnipersTableColumn.LAST_PRICE, 555);
        assertColumnEquals(SnipersTableColumn.LAST_BID, 666);
        assertColumnEquals(SnipersTableColumn.SNIPER_STATE, SnipersTableModel.textFor(SniperState.BIDDING));
    }

    private void assertColumnEquals(SnipersTableColumn column, Object expected) {
        final int rowIndex = 0;
        final int columnIndex = column.ordinal();
        assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
    }

    private Matcher<TableModelEvent> aRowChangedEvent() {
        return samePropertyValuesAs(new TableModelEvent(model, 0));
    }
}
