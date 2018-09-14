package team578.lib.swerve;

public class SwerveCommandComponents {
	public double x1, x2, x3, x4;
	public double y1, y2, y3, y4;

	static public SwerveCommandComponents add(final SwerveCommandComponents A, final SwerveCommandComponents B) {
		final SwerveCommandComponents C = new SwerveCommandComponents();

		C.x1 = A.x1 + B.x1;
		C.y1 = A.y1 + B.y1;

		C.x2 = A.x2 + B.x2;
		C.y2 = A.y2 + B.y2;

		C.x3 = A.x3 + B.x3;
		C.y3 = A.y3 + B.y3;

		C.x4 = A.x4 + B.x4;
		C.y4 = A.y4 + B.y4;

		return C;
	}

	public SwerveCommands toCommands() {
		final SwerveCommands commands = new SwerveCommands();

		commands.speed1 = SwerveDrive.calcSpeed(x1, y1);
		commands.speed2 = SwerveDrive.calcSpeed(x2, y2);
		commands.speed3 = SwerveDrive.calcSpeed(x3, y3);
		commands.speed4 = SwerveDrive.calcSpeed(x4, y4);

		commands.angle1 = SwerveDrive.calcAngle(x1, y1);
		commands.angle2 = SwerveDrive.calcAngle(x2, y2);
		commands.angle3 = SwerveDrive.calcAngle(x3, y3);
		commands.angle4 = SwerveDrive.calcAngle(x4, y4);

		return commands;
	}
}
