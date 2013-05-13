package com.thewizardplusplus.diaryofcalories;

public class IntegerVector2D {
	public static IntegerVector2D add(IntegerVector2D vector1, IntegerVector2D
		vector2)
	{
		IntegerVector2D result = new IntegerVector2D();
		result.x = vector1.x + vector2.x;
		result.y = vector1.y + vector2.y;
		return result;
	}

	public static IntegerVector2D sub(IntegerVector2D vector1, IntegerVector2D
		vector2)
	{
		IntegerVector2D result = new IntegerVector2D();
		result.x = vector1.x - vector2.x;
		result.y = vector1.y - vector2.y;
		return result;
	}

	public static IntegerVector2D mul(IntegerVector2D vector1, IntegerVector2D
		vector2)
	{
		IntegerVector2D result = new IntegerVector2D();
		result.x = vector1.x * vector2.x;
		result.y = vector1.y * vector2.y;
		return result;
	}

	public static IntegerVector2D mul(IntegerVector2D vector, long number) {
		IntegerVector2D result = new IntegerVector2D();
		result.x = vector.x * number;
		result.y = vector.y * number;
		return result;
	}

	public static IntegerVector2D div(IntegerVector2D vector, long number) {
		IntegerVector2D result = new IntegerVector2D();
		result.x = Math.round((double)vector.x / number);
		result.y = Math.round((double)vector.y / number);
		return result;
	}

	public long x;
	public long y;

	public IntegerVector2D() {
		this.x = 0;
		this.y = 0;
	}

	public IntegerVector2D(long x, long y) {
		this.x = x;
		this.y = y;
	}

	public IntegerVector2D add(IntegerVector2D vector) {
		x += vector.x;
		y += vector.y;
		return this;
	}

	public IntegerVector2D sub(IntegerVector2D vector) {
		x -= vector.x;
		y -= vector.y;
		return this;
	}

	public IntegerVector2D mul(IntegerVector2D vector) {
		x *= vector.x;
		y *= vector.y;
		return this;
	}

	public IntegerVector2D mul(long number) {
		x *= number;
		y *= number;
		return this;
	}

	public IntegerVector2D div(long number) {
		x = Math.round((double)x / number);
		y = Math.round((double)y / number);
		return this;
	}

	public long length() {
		return Math.round(Math.sqrt(x * x + y * y));
	}
}
