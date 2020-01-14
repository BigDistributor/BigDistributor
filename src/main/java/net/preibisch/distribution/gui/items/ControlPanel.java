package net.preibisch.distribution.gui.items;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.preibisch.distribution.algorithm.clustering.workflow.Flow;
import net.preibisch.distribution.algorithm.clustering.workflow.Workflow;
import net.preibisch.distribution.tools.Helper;

public class ControlPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -5489935889866505715L;

//	public ParamsPanel paramsPanel;
	public Button startWorkFlowButton;
	public Button generateResultButton;
	public InputPanel inputPanel;
	public SliderPanel sliderOverlapPanel;
	public JLabel numberBlocksLabel;
	public BlockSizeControlPanel blockSizeControlPanel;

	public ControlPanel(int dimensions) {
		new Workflow();
		blockSizeControlPanel = new BlockSizeControlPanel(DataPreview.getDims().length);

		numberBlocksLabel = new JLabel("Total Blocks: 0", SwingConstants.CENTER);

		setLayout(new GridLayout(10, 1, 20, 20));
		sliderOverlapPanel = new SliderPanel(-1, "Overlap:", 0, 200, 10);

		startWorkFlowButton = new Button("START..");
		generateResultButton = new Button("Get and Generate Result");

		JPanel jobsPanel = new JPanel();
		jobsPanel.setLayout(new GridLayout(1, 2, 10, 10));
		Label jobLabel = new Label("Block per job:");
		jobLabel.setAlignment(Label.RIGHT);
		jobsPanel.add(jobLabel);
		this.add(Workflow.progressBarPanel);
		this.add(Helper.createImagePanel("img/labels.png"));

		this.add(numberBlocksLabel);
		for (SliderPanel slider : blockSizeControlPanel.sliderPanels) {
			this.add(slider);
		}
		this.add(sliderOverlapPanel);
		this.add(jobsPanel);
		this.add(startWorkFlowButton);
		this.add(generateResultButton);

		startWorkFlowButton.addActionListener(this);

		generateResultButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startWorkFlowButton) {
			try {
				Workflow.run(Flow.START_FLOW);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (e.getSource() == generateResultButton) {
			try {
				Workflow.run(Flow.RESULT_FLOW);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}