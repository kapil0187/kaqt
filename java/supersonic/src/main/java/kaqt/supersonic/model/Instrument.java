package kaqt.supersonic.model;

public class Instrument {
	private String mmy;
	private String tick_incr;
	private String symbol;
	private String denominator;
	private String numerator;
	private String instrument;
	private String init_margin;
	private String multiplier;
	private String id;
	private String _id;
	private String maturity_date;
	private String description;
	private String min_px_incr;
	private String maint_margin;
	private String exchange;
	
	public Instrument(String mmy, String tick_incr, String symbol,
			String denominator, String numerator, String instrument,
			String init_margin, String multiplier, String id,
			String _id, String maturity_date,
			String description, String min_px_incr, String maint_margin,
			String exchange) {
		super();
		this.mmy = mmy;
		this.tick_incr = tick_incr;
		this.symbol = symbol;
		this.denominator = denominator;
		this.numerator = numerator;
		this.instrument = instrument;
		this.init_margin = init_margin;
		this.multiplier = multiplier;
		this.id = id;
		this._id = _id;
		this.maturity_date = maturity_date;
		this.description = description;
		this.min_px_incr = min_px_incr;
		this.maint_margin = maint_margin;
		this.exchange = exchange;
	}

	public String getMmy() {
		return mmy;
	}

	public void setMmy(String mmy) {
		this.mmy = mmy;
	}

	public String getTick_incr() {
		return tick_incr;
	}

	public void setTick_incr(String tick_incr) {
		this.tick_incr = tick_incr;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getDenominator() {
		return denominator;
	}

	public void setDenominator(String denominator) {
		this.denominator = denominator;
	}

	public String getNumerator() {
		return numerator;
	}

	public void setNumerator(String numerator) {
		this.numerator = numerator;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getInit_margin() {
		return init_margin;
	}

	public void setInit_margin(String init_margin) {
		this.init_margin = init_margin;
	}

	public String getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(String multiplier) {
		this.multiplier = multiplier;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getMaturity_date() {
		return maturity_date;
	}

	public void setMaturity_date(String maturity_date) {
		this.maturity_date = maturity_date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMin_px_incr() {
		return min_px_incr;
	}

	public void setMin_px_incr(String min_px_incr) {
		this.min_px_incr = min_px_incr;
	}

	public String getMaint_margin() {
		return maint_margin;
	}

	public void setMaint_margin(String maint_margin) {
		this.maint_margin = maint_margin;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	@Override
	public String toString() {
		return "Instrument [_id = " + _id
				+ ", mmy = " + mmy + ", tick_incr = " + tick_incr
				+ ", symbol = " + symbol + ", denominator = " + denominator
				+ ", numerator = " + numerator + ", instrument = " + instrument
				+ ", init_margin = " + init_margin + ", multiplier = "
				+ multiplier + ", id = " + id + ",maturity_date = " + maturity_date + ", description = "
				+ description + ", min_px_incr = " + min_px_incr
				+ ", maint_margin = " + maint_margin + ", exchange = "
				+ exchange + "]";
	}
}
