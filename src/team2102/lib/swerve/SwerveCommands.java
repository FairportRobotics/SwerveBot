package team2102.lib.swerve;

// Uses degrees for angles
public class SwerveCommands {
	public double speed1, speed2, speed3, speed4;
	public double angle1, angle2, angle3, angle4;

	public SwerveCommandComponents toComponents() {
		final SwerveCommandComponents components = new SwerveCommandComponents();

		components.x1 = speed1 * Math.sin(Math.toRadians(angle1));
		components.y1 = speed1 * Math.cos(Math.toRadians(angle1));

		components.x2 = speed2 * Math.sin(Math.toRadians(angle2));
		components.y2 = speed2 * Math.cos(Math.toRadians(angle2));

		components.x3 = speed3 * Math.sin(Math.toRadians(angle3));
		components.y3 = speed3 * Math.cos(Math.toRadians(angle3));

		components.x4 = speed4 * Math.sin(Math.toRadians(angle4));
		components.y4 = speed4 * Math.cos(Math.toRadians(angle4));

		return components;
	}
}
