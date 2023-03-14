package me.syberiak.cmdr.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class CheckList extends JList<CheckListItem> {
    public CheckList(String[] items) {
        DefaultListModel<CheckListItem> model = new DefaultListModel<>();
        for (String item : items) {
            model.addElement(new CheckListItem(item));
        }
        setModel(model);
        setCellRenderer(new CheckListRenderer<>());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JList<CheckListItem> list = (JList<CheckListItem>) event.getSource();
                int index = list.locationToIndex(event.getPoint());// Get index of item
                // clicked
                CheckListItem item = list.getModel().getElementAt(index);
                item.setSelected(!item.isSelected()); // Toggle selected state
                list.repaint(list.getCellBounds(index, index));// Repaint cell
            }
        });
    }

    public CheckListItem[] getItems() {
        ArrayList<CheckListItem> items = new ArrayList<>();
        ListModel<CheckListItem> model = getModel();

        for (int i = 0; i < model.getSize(); i++) {
            CheckListItem item = model.getElementAt(i);
            items.add(item);
        }

        return items.toArray(new CheckListItem[0]);
    }
    public String[] getItemsLabels() {
        return Arrays.stream(getItems()).map(CheckListItem::toString).toArray(String[]::new);
    }

    public CheckListItem[] getSelectedItems() {
        return Arrays.stream(getItems()).filter(CheckListItem::isSelected).toArray(CheckListItem[]::new);
    }
    public String[] getSelectedItemsLabels() {
        return Arrays.stream(getSelectedItems()).map(CheckListItem::toString).toArray(String[]::new);
    }
}


class CheckListRenderer<E> extends JCheckBox implements ListCellRenderer<E> {
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        setEnabled(list.isEnabled());
        setSelected(((CheckListItem) value).isSelected());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setText(value.toString());
        return this;
    }
}