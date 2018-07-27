package team2102.lib.swerve;

public class SwerveDrive {

	private final double m_length, m_width, m_diagonal;
	private final SwerveModule m_module1, m_module2, m_module3, m_module4;

	public SwerveDrive(final SwerveModule module1, final SwerveModule module2, final SwerveModule module3,
			final SwerveModule module4, final double length, final double width) {
		m_module1 = module1;
		m_module2 = module2;
		m_module3 = module3;
		m_module4 = module4;

		m_length = length;
		m_width = width;
		m_diagonal = Math.sqrt(length * length + width * width);
	}

	public void drive(double forward, double strafe, double rotate) {
		final SwerveCommands commands = calcModuleCommands(forward, strafe, rotate);

		// Should this be available outside this class?
		final double max = Math.max(Math.max(Math.max(commands.speed1, commands.speed2), commands.speed3),
				commands.speed4);
		if (max > 1.0) {
			commands.speed1 /= max;
			commands.speed2 /= max;
			commands.speed3 /= max;
			commands.speed4 /= max;
		}

		driveCommands(commands);
	}

	public void driveCommands(final SwerveCommands commands) {
		m_module1.setPower(commands.speed1);
		m_module2.setPower(commands.speed2);
		m_module3.setPower(commands.speed3);
		m_module4.setPower(commands.speed4);

		m_module1.setAngle(commands.angle1);
		m_module2.setAngle(commands.angle2);
		m_module3.setAngle(commands.angle3);
		m_module4.setAngle(commands.angle4);
	}

	public void stop() {
		m_module1.setPower(0.0);
		m_module2.setPower(0.0);
		m_module3.setPower(0.0);
		m_module4.setPower(0.0);
	}

	public SwerveCommands calcModuleCommands(double forward, double strafe, double rotate) {
		final SwerveCommands commands = new SwerveCommands();

		final double a = strafe - rotate * (m_length / m_diagonal);
		final double b = strafe + rotate * (m_length / m_diagonal);
		final double c = forward - rotate * (m_width / m_diagonal);
		final double d = forward + rotate * (m_width / m_diagonal);

		commands.speed1 = calcSpeed(b, d);
		commands.speed2 = calcSpeed(b, c);
		commands.speed3 = calcSpeed(a, c);
		commands.speed4 = calcSpeed(a, d);

		commands.angle1 = calcAngle(b, d);
		commands.angle2 = calcAngle(b, c);
		commands.angle3 = calcAngle(a, c);
		commands.angle4 = calcAngle(a, d);

		return commands;
	}

	public static double calcSpeed(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}

	public static double calcAngle(double x, double y) {
		return Math.atan2(x, y) / Math.PI * 180.0;
	}
}
