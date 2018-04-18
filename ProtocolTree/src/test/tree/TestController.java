package test.tree;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.tree.DefaultTreeCellRenderer;

public class TestController {
	TestNodeContent model;
	Mode mode = Mode.RENDER;
	TestEditRenderPanel panel;

	public TestController() {
		panel = new TestEditRenderPanel();// this creates the JLabels
	}

	public void fillUiFromModel() {
		panel.setField1LabelText(model.getString1());
		panel.setField2LabelText(model.getString2());
	}

	public void fillModelFromUi() {
		model.field1 = panel.field1Component.getText();
		model.field2 = panel.field2Component.getText();
	}

	public void setField1LabelText(String string1) {
		panel.setField1LabelText(string1);
	}

	public void setField2LabelText(String string2) {
		panel.setField1LabelText(string2);
	}

	public TestEditRenderPanel getPanel() {
		return panel;
	}

	public void setModel(TestNodeContent nodeContent) {
		this.model = nodeContent;
	}

	public Object getModel() {
		return model;
	}

	@Override
	public String toString() {
		return "Controller [model=" + model + ", panel=" + panel + "]";
	}

	public void setMode(Mode mode) {
		System.out.println("Modes set to:"+mode);
		this.mode = mode;
		if (mode == Mode.RENDER) {
			getPanel().setBorder(null);
		} else {
			getPanel().setBorder(BorderFactory.createLineBorder(Color.RED));
		}
	}


	public class NodeContent{


		String field1;
		String field2="DUMMY STRING";
		NodeContent(String s){
			field1 =s;
		}
		public String getString1() {
			return field1;
		}

		public void setString1(String string1) {
			this.field1 = string1;
		}
		public String getString2() {
			return field2;
		}

		public void setString2(String string2) {
			this.field2 = string2;
		}
		@Override
		public String toString() {
			return "NodeContent [field1=" + field1 + ", field2=" + field2 + "]";
		}
	}
	
	
}

enum Mode {
	RENDER, EDIT
}
