package com.varunsingh.kalmanfilter;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

public class Calculator {
	private static JLabel answer;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		setLookAndFeel();
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 10, 10);
		frame.setLayout(layout);
		frame.setVisible(true);
		frame.setSize(600, 400);
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Kalman Filter Calculator");

		JTextField observationError = new JTextField("Observation Error");
		frame.add(observationError);
		observationError.setColumns(10);

		JTextField processError = new JTextField("Process Error");
		frame.add(processError);

		JTextField initialEstimate = new JTextField("Initial Estimate");
		frame.add(initialEstimate);

		JTextField measurement = new JTextField("Measurement");
		frame.add(measurement);

		answer = new JLabel();
		frame.add(answer);

		JButton findAnswer = new JButton("Calculate");
		findAnswer.addActionListener(e -> {
			answer.setText(
				updateAnswer(
					Double.parseDouble(observationError.getText()), Double
						.parseDouble(initialEstimate.getText()), Double
							.parseDouble(processError.getText()), Double
								.parseDouble(measurement.getText())
				)
			);
		});
		frame.add(findAnswer);

		frame.revalidate();
	}

	static String updateAnswer(double observationError, double initialEstimate, double processError, double measurement) {
		KalmanFilter kf = new KalmanFilter(Vector.column(observationError, 20));

		Calculations updatedState = kf.execute(
			new Calculations(
				Vector.column(initialEstimate, 2), (Matrix) kf
					.initialProcessCovariance(Vector.column(processError, 5))
			), Vector.column(measurement, 20)
		);

		return String.valueOf(updatedState.stateEstimate().get(0));
	}

	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(
				"com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"
			);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
