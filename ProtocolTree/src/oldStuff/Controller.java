package oldStuff;
//package ui.property;
//
//import java.awt.Color;
//
//import javax.swing.BorderFactory;
//import javax.swing.Icon;
//import javax.swing.JPanel;
//import javax.swing.UIManager;
//import javax.swing.tree.DefaultTreeCellRenderer;
//
//public class Controller {
//	NodeContentModel model;
//	Mode mode = Mode.RENDER;
//	EditRenderPanel panel;
//
//	public Controller() {
//		panel = new EditRenderPanel();// this creates the JLabels
//	}
//
//	public void fillUiFromModel() {
//		panel.setLocal(model.getLocal());
//		panel.setValue(model.getValue());
//		panel.setRange(model.getRange());
//		panel.setDomain(model.getDomain());
//		if (true) {
//			panel.setIcon(EditRenderPanel.iconObjectProperty);
//		} else {
//			panel.setIcon(EditRenderPanel.iconDataProperty);
//		}
//	}
//
//	public void fillModelFromUi() {
//		model=new NodeContentModel(panel.getLocal(), panel.getValue(),panel.getRange(),panel.getDomain());
////		model.setLocal(panel.getLocal());
////		model.setValue(panel.getValue());
////		model.setRange(panel.getRange());
////		model.setDomain(panel.getDomain());
//	}
//
//	// public void setField1LabelText(String string1) {
//	// panel.setField1LabelText(string1);
//	// }
//	//
//	// public void setField2LabelText(String string2) {
//	// panel.setField1LabelText(string2);
//	// }
//
//	public EditRenderPanel getPanel() {
//		return panel;
//	}
//
//	public void setModel(NodeContentModel model) {
//		this.model = model;
//	}
//
//	public Object getModel() {
//		return model;
//	}
//
//	@Override
//	public String toString() {
//		return "Controller [model=" + model + ", panel=" + panel + "]";
//	}
//
//	public void setMode(Mode mode) {
//		System.out.println("Modes set to:" + mode);
//		this.mode = mode;
//		if (mode == Mode.RENDER) {
//			getPanel().setBorder(null);
//		} else {
//			getPanel().setBorder(BorderFactory.createLineBorder(Color.RED));
//		}
//	}
//}
//
//enum Mode {
//	RENDER, EDIT
//}
