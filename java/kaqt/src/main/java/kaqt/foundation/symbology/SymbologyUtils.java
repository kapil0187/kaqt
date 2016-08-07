package kaqt.foundation.symbology;

public class SymbologyUtils
{
	public static enum Product
	{
		ALL, GE, CL, NG, ES
	}

	public static enum Exchange
	{
		ALL, CME, ICE
	}

	public static enum FuturesMonthCode
	{
		F(1), G(2), H(3), J(4), K(5), M(6), N(7), Q(8), U(9), V(10), X(11), Z(
				12);

		private final int monthNum;

		FuturesMonthCode(int monthNum)
		{
			this.monthNum = monthNum;
		}

		public int getValue()
		{
			return monthNum;
		}
	}
}
