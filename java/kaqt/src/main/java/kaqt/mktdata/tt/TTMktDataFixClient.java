package kaqt.mktdata.tt;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.log4j.Logger;

import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.field.AggregatedBook;
import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.MDUpdateType;
import quickfix.field.MarketDepth;
import quickfix.field.SecurityExchange;
import quickfix.field.SecurityID;
import quickfix.field.SecurityType;
import quickfix.field.SubscriptionRequestType;
import quickfix.field.Symbol;
import quickfix.fix42.MarketDataRequest;
import quickfix.fix42.MarketDataRequestReject;
import quickfix.fix42.MarketDataRequest.NoMDEntryTypes;
import quickfix.fix42.MarketDataRequest.NoRelatedSym;
import quickfix.fix42.MarketDataSnapshotFullRefresh;
import quickfix.fix42.MarketDataSnapshotFullRefresh.NoMDEntries;
import kaqt.foundation.connections.FixClient;
import kaqt.foundation.mktdata.Quote;
import kaqt.foundation.symbology.SymbologyUtils;
import kaqt.providers.protobuf.Symbology.FuturesInstrument;
import kaqt.utils.FixMessageHelper;

public class TTMktDataFixClient extends FixClient
{
	private static Logger logger = Logger.getLogger(TTMktDataFixClient.class);

	private Consumer<Quote> quoteConsumer;
	private int depth = -1;
	private Map<String, FuturesInstrument> instdefReference;
	private SessionID priceSessionId;
	private Map<String, String> subscriptionIds;

	public TTMktDataFixClient(String configFile, Consumer<Quote> quoteConsumer,
			Map<String /* TT ID */, FuturesInstrument> instdefReference)
	{
		super(configFile);
		
		this.quoteConsumer = quoteConsumer;
		this.instdefReference = instdefReference;
		this.subscriptionIds = new HashMap<String, String>();
	}

	public TTMktDataFixClient(String configFile, Consumer<Quote> quoteConsumer,
			Map<String /* TT ID */, FuturesInstrument> instdefReference,
			int depth)
	{
		this(configFile, quoteConsumer, instdefReference);
		this.depth = depth;
	}

	@Override
	public void onLogon(SessionID sessionId)
	{
		this.priceSessionId = sessionId;
		super.onLogon(sessionId);
		if (depth == -1)
		{
			instdefReference
					.keySet()
					.stream()
					.forEach(
							s -> this
									.subscribeTopOfBook(new ImmutableTriple<SymbologyUtils.Product, SymbologyUtils.Exchange, String>(
											SymbologyUtils.Product
													.valueOf(this.instdefReference
															.get(s)
															.getUnderlying()),
											SymbologyUtils.Exchange
													.valueOf(this.instdefReference
															.get(s)
															.getExchangeGroup()),
											s)));
		}
		else
		{
			instdefReference
					.keySet()
					.stream()
					.forEach(
							s -> this
									.subscribeDepthOfBook(
											new ImmutableTriple<SymbologyUtils.Product, SymbologyUtils.Exchange, String>(
													SymbologyUtils.Product
															.valueOf(this.instdefReference
																	.get(s)
																	.getUnderlying()),
													SymbologyUtils.Exchange
															.valueOf(this.instdefReference
																	.get(s)
																	.getExchangeGroup()),
													s), this.depth));
		}
	}

	@Override
	public void toAdmin(Message message, SessionID sessionId)
	{
		message = FixMessageHelper.passwordify(message, super.getPassword());
		super.toAdmin(message, sessionId);
	}

	@Override
	public void onMessage(MarketDataSnapshotFullRefresh message,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue
	{
		Quote quote = FixMessageHelper.createQuote(message, this.instdefReference);
		quoteConsumer.accept(quote);
	}

	@Override
	public void onMessage(MarketDataRequestReject message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue
	{
		logger.error("MARKET DATA REQUEST REJECT!!");
		logger.error(message);
	}
	
	private void subscribeTopOfBook(
			Triple<SymbologyUtils.Product, SymbologyUtils.Exchange, String> securityId)
	{
		logger.info(String.format("SUBSCRIBING TOP OF THE BOOK FOR %s",
				securityId));
		MarketDataRequest mdr = new MarketDataRequest();
		UUID uuid = UUID.randomUUID();
		subscriptionIds.put(securityId.getRight(), uuid.toString());
		mdr.set(new MDReqID(uuid.toString()));
		mdr.set(new SubscriptionRequestType(
				SubscriptionRequestType.SNAPSHOT_PLUS_UPDATES));
		mdr.set(new MarketDepth(1));
		mdr.set(new MDUpdateType(MDUpdateType.FULL_REFRESH));
		mdr.set(new AggregatedBook(true));
		
		quickfix.field.NoMDEntryTypes noMdEntryTypes = new quickfix.field.NoMDEntryTypes();
		noMdEntryTypes.setValue(3);
		mdr.set(noMdEntryTypes);
		
		NoMDEntryTypes noMdBid = new NoMDEntryTypes();
		noMdBid.set(new MDEntryType(MDEntryType.BID));
		mdr.addGroup(noMdBid);

		NoMDEntryTypes noMdAsk = new NoMDEntryTypes();
		noMdAsk.set(new MDEntryType(MDEntryType.OFFER));
		mdr.addGroup(noMdAsk);

		NoMDEntryTypes noMdTrade = new NoMDEntryTypes();
		noMdTrade.set(new MDEntryType(MDEntryType.TRADE));
		mdr.addGroup(noMdTrade);
		
		NoRelatedSym noRelSym = new NoRelatedSym();
		noRelSym.set(new Symbol(securityId.getLeft().name()));
		noRelSym.set(new SecurityExchange(securityId.getMiddle().name()));
		noRelSym.set(new SecurityID(securityId.getRight()));
		noRelSym.set(new SecurityType(SecurityType.FUTURE));
		mdr.addGroup(noRelSym);
		
		MarketDataRequest mdrCorrect = new MarketDataRequest();
		
//		try
//		{
//			mdrCorrect.fromString(mdr.toString(), 
//					new DataDictionary("/Users/kapilsharma/GitHub/kaqt/config/TT_FIX42.xml"), true);
//		}
//		catch (InvalidMessage e1)
//		{
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		catch (ConfigError e1)
//		{
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		try
		{
			System.out.println(mdr);
			Session.sendToTarget(mdr, this.priceSessionId);
		}
		catch (SessionNotFound e)
		{
			e.printStackTrace();
		}
	}

	private void unsubscribeTopOfBook(
			Triple<SymbologyUtils.Product, SymbologyUtils.Exchange, String> securityId)
	{
		logger.info(String.format("UNSUBSCRIBING FROM TOP OF THE BOOK FOR %s",
				securityId));
		MarketDataRequest mdr = new MarketDataRequest();
		mdr.set(new MDReqID(subscriptionIds.get(securityId.getRight())));
		mdr.set(new SubscriptionRequestType(
				SubscriptionRequestType.DISABLE_PREVIOUS_SNAPSHOT_PLUS_UPDATE_REQUEST));
		mdr.set(new MarketDepth(1));
		mdr.set(new MDUpdateType(MDUpdateType.FULL_REFRESH));
		mdr.set(new AggregatedBook(true));
		
		quickfix.field.NoMDEntryTypes noMdEntryTypes = new quickfix.field.NoMDEntryTypes();
		noMdEntryTypes.setValue(3);
		mdr.set(noMdEntryTypes);
		
		NoMDEntryTypes noMdBid = new NoMDEntryTypes();
		noMdBid.set(new MDEntryType(MDEntryType.BID));
		mdr.addGroup(noMdBid);

		NoMDEntryTypes noMdAsk = new NoMDEntryTypes();
		noMdAsk.set(new MDEntryType(MDEntryType.OFFER));
		mdr.addGroup(noMdAsk);

		NoMDEntryTypes noMdTrade = new NoMDEntryTypes();
		noMdTrade.set(new MDEntryType(MDEntryType.TRADE));
		mdr.addGroup(noMdTrade);
		
		NoRelatedSym noRelSym = new NoRelatedSym();
		noRelSym.set(new Symbol(securityId.getLeft().name()));
		noRelSym.set(new SecurityExchange(securityId.getMiddle().name()));
		noRelSym.set(new SecurityID(securityId.getRight()));
		noRelSym.set(new SecurityType(SecurityType.FUTURE));
		mdr.addGroup(noRelSym);	
		
		try
		{
			Session.sendToTarget(mdr, this.priceSessionId);
		}
		catch (SessionNotFound e)
		{
			e.printStackTrace();
		}
	}

	private void subscribeDepthOfBook(
			Triple<SymbologyUtils.Product, SymbologyUtils.Exchange, String> securityId,
			int depth)
	{
		logger.info(String.format("SUBSCRIBING DEPTH OF THE BOOK FOR %s",
				securityId));
	}

	private void unsubscribeDepthOfBook(
			Triple<SymbologyUtils.Product, SymbologyUtils.Exchange, String> securityId,
			int depth)
	{
		logger.info(String.format(
				"UNSUBSCRIBING FROM DEPTH OF THE BOOK FOR %s", securityId));
	}

	@Override
	public void stop()
	{
		if (depth == -1)
		{
			instdefReference
					.keySet()
					.stream()
					.forEach(
							s -> this
									.unsubscribeTopOfBook(new ImmutableTriple<SymbologyUtils.Product, SymbologyUtils.Exchange, String>(
											SymbologyUtils.Product
													.valueOf(this.instdefReference
															.get(s)
															.getUnderlying()),
											SymbologyUtils.Exchange
													.valueOf(this.instdefReference
															.get(s)
															.getExchangeGroup()),
											s)));
		}
		else
		{
			instdefReference
					.keySet()
					.stream()
					.forEach(
							s -> this
									.unsubscribeDepthOfBook(
											new ImmutableTriple<SymbologyUtils.Product, SymbologyUtils.Exchange, String>(
													SymbologyUtils.Product
															.valueOf(this.instdefReference
																	.get(s)
																	.getUnderlying()),
													SymbologyUtils.Exchange
															.valueOf(this.instdefReference
																	.get(s)
																	.getExchangeGroup()),
													s), this.depth));
		}

		super.stop();
	}
}
