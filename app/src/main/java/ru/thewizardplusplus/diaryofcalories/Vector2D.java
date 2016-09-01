package ru.thewizardplusplus.diaryofcalories;

public class Vector2D {
	public static Vector2D add(Vector2D vector1, Vector2D vector2) {
		return new Vector2D(
			vector1.x + vector2.x,
			vector1.y + vector2.y
		);
	}

	public static Vector2D sub(Vector2D vector1, Vector2D vector2) {
		return new Vector2D(
			vector1.x - vector2.x,
			vector1.y - vector2.y
		);
	}

	public static Vector2D mul(Vector2D vector1, Vector2D vector2) {
		return new Vector2D(
			vector1.x * vector2.x,
			vector1.y * vector2.y
		);
	}

	public static Vector2D mul(Vector2D vector, double number) {
		return new Vector2D(
			vector.x * number,
			vector.y * number
		);
	}

	public static Vector2D div(Vector2D vector, double number) {
		return new Vector2D(
			Math.round(vector.x / number),
			Math.round(vector.y / number)
		);
	}

	public double x = 0.0;
	public double y = 0.0;

	public Vector2D() {}

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public boolean isNull() {
		return x == 0.0 && y == 0.0;
	}

	public Vector2D add(Vector2D vector) {
		x += vector.x;
		y += vector.y;

		return this;
	}

	public Vector2D sub(Vector2D vector) {
		x -= vector.x;
		y -= vector.y;

		return this;
	}

	public Vector2D mul(Vector2D vector) {
		x *= vector.x;
		y *= vector.y;

		return this;
	}

	public Vector2D mul(double number) {
		x *= number;
		y *= number;

		return this;
	}

	public Vector2D div(double number) {
		x /= number;
		y /= number;

		return this;
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}
}
