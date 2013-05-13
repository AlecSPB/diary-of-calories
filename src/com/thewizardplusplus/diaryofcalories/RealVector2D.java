package com.thewizardplusplus.diaryofcalories;

public class RealVector2D {
	public static RealVector2D add(RealVector2D vector1, RealVector2D vector2) {
		RealVector2D result = new RealVector2D();
		result.x = vector1.x + vector2.x;
		result.y = vector1.y + vector2.y;
		return result;
	}

	public static RealVector2D sub(RealVector2D vector1, RealVector2D vector2) {
		RealVector2D result = new RealVector2D();
		result.x = vector1.x - vector2.x;
		result.y = vector1.y - vector2.y;
		return result;
	}

	public static RealVector2D mul(RealVector2D vector1, RealVector2D vector2) {
		RealVector2D result = new RealVector2D();
		result.x = vector1.x * vector2.x;
		result.y = vector1.y * vector2.y;
		return result;
	}

	public static RealVector2D mul(RealVector2D vector, double number) {
		RealVector2D result = new RealVector2D();
		result.x = vector.x * number;
		result.y = vector.y * number;
		return result;
	}

	public static RealVector2D div(RealVector2D vector, double number) {
		RealVector2D result = new RealVector2D();
		result.x = Math.round((double)vector.x / number);
		result.y = Math.round((double)vector.y / number);
		return result;
	}

	public double x;
	public double y;

	public RealVector2D() {
		this.x = 0.0;
		this.y = 0.0;
	}

	public RealVector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public RealVector2D add(RealVector2D vector) {
		x += vector.x;
		y += vector.y;
		return this;
	}

	public RealVector2D sub(RealVector2D vector) {
		x -= vector.x;
		y -= vector.y;
		return this;
	}

	public RealVector2D mul(RealVector2D vector) {
		x *= vector.x;
		y *= vector.y;
		return this;
	}

	public RealVector2D mul(double number) {
		x *= number;
		y *= number;
		return this;
	}

	public RealVector2D div(double number) {
		x /= number;
		y /= number;
		return this;
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}
}
