package burp.tab;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import burp.IHttpRequestResponse;
import burp.ITextEditor;
import burp.pocs.IPoc;
import burp.pocs.Pocs;

public class POCTypesComboBox extends JComboBox<String> implements ItemListener {

	private static final long serialVersionUID = 1L;
	private IHttpRequestResponse request;
	private ITextEditor textEditor;
	private Pocs pocs;

	public POCTypesComboBox(ITextEditor textEditor, IHttpRequestResponse request, Pocs pocs) {
		this.textEditor = textEditor;
		this.request = request;
		this.pocs = pocs;
		Iterator<String> pocKeys = pocs.getPocKeys();
		while (pocKeys.hasNext()) {
			addItem(pocKeys.next());
		}
		addItemListener(this);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		String selectedItem = getSelectedItem().toString();
        IPoc poc = this.pocs.getPoc(selectedItem);
        try {
            byte[] pocContent = poc.getPoc(this.request);
            this.textEditor.setText(pocContent);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
        } 
	}

}
