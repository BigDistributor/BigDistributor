package main.java.net.preibisch.distribution.gui.items;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.java.net.preibisch.distribution.algorithm.clustering.workflow.Flow;
import main.java.net.preibisch.distribution.algorithm.clustering.workflow.Workflow;
import main.java.net.preibisch.distribution.algorithm.controllers.logmanager.MyLogger;
import main.java.net.preibisch.distribution.tools.Helper;
import main.java.net.preibisch.distribution.tools.config.Config;

public class ControlPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -5489935889866505715L;

//	public ParamsPanel paramsPanel;
	public Button startWorkFlowButton;
	public Button generateResultButton;
	public InputPanel inputPanel;
	public SliderPanel sliderOverlapPanel;
	public JLabel numberBlocksLabel;
	public JTextField jobsField;
	public BlockSizeControlPanel blockSizeControlPanel;
	

	public ControlPanel(int dimensions) {
		new Workflow();
		blockSizeControlPanel = new BlockSizeControlPanel(Config.getDataPreview().getFile().getDimensions().length);

		numberBlocksLabel = new JLabel("Total Blocks: 0", SwingConstants.CENTER);

		setLayout(new GridLayout(10,1, 20, 20));
		sliderOverlapPanel = new SliderPanel(-1, "Overlap:", 0, 200, 10);

		startWorkFlowButton = new Button("START..");
		generateResultButton = new Button("Get and Generate Result");

		jobsField = new JTextField("10");
		JPanel jobsPanel = new JPanel();
		jobsPanel.setLayout(new GridLayout(1, 2, 10, 10));
		Label jobLabel = new Label("Block per job:");
		jobLabel.setAlignment(Label.RIGHT);
		jobsPanel.add(jobLabel);
		jobsPanel.add(jobsField);
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
				Config.parallelJobs = Integer.parseInt(jobsField.getText());
			} catch (Exception ex) {
				MyLogger.log.error("Invalide Task number! putted default 10 Jobs");
				Config.parallelJobs = 10;
			}
			

			Workflow.run(Flow.START_FLOW);;
		}
		if (e.getSource() == generateResultButton) {
			Workflow.run(Flow.RESULT_FLOW);
		}
	}
}