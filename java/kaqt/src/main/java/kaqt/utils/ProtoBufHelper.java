package kaqt.utils;

import java.util.List;

import org.apache.log4j.Logger;

import kaqt.foundation.db.DbFuturesInstrument;
import kaqt.foundation.mktdata.Quote;
import kaqt.providers.protobuf.LeveloneQuote;
import kaqt.providers.protobuf.LeveloneQuote.LevelOneQuote;
import kaqt.providers.protobuf.Symbology.AlternateId;
import kaqt.providers.protobuf.Symbology.FuturesInstrument;
import kaqt.providers.protobuf.Symbology.FuturesInstrumentRequest;
import kaqt.providers.protobuf.Symbology.FuturesInstrumentResponse;
import kaqt.symbology.SymbologyZmqServer;

public class ProtoBufHelper
{
	public static LevelOneQuote createLevelOneQuote(Quote quote)
	{
		LevelOneQuote.Builder rvBuiilder = LevelOneQuote
				.newBuilder();
		rvBuiilder.setInstrumentId(quote.getId()).setBid(quote.getBidPrice())
				.setAsk(quote.getAskPrice()).setBidQty(quote.getBidQty())
				.setAskQty(quote.getAskQty()).setLast(quote.getLastPrice())
				.setLastQty(quote.getLastQty())
				.setPosixTime(quote.getPosixTime());
		return rvBuiilder.build();
	}

	public static Quote createQuote(LevelOneQuote l1quote)
	{
		return new Quote(l1quote.getInstrumentId(), 
				l1quote.getBid(), l1quote.getAsk(), l1quote.getLast(), 
				l1quote.getBidQty(), l1quote.getAskQty(), l1quote.getLastQty(), l1quote.getPosixTime());
	}
	
	public static FuturesInstrument createFuturesInstrument(
			DbFuturesInstrument instrument)
	{
		FuturesInstrument.Builder rvBuilder = FuturesInstrument.newBuilder()
				.setId(instrument.getKey()).setTicker(instrument.getTicker())
				.setUnderlying(instrument.getUnderlying())
				.setDescription(instrument.getDescription())
				.setExchange(instrument.getExchange())
				.setExchangeGroup(instrument.getExchangeGroup())
				.setExpiryPosixDatetime(instrument.getExpiryPosixDatetime())
				.setMinOrderSize(instrument.getMinOrderSize())
				.setTickSize(instrument.getTickSize())
				.setTradeableTickSize(instrument.getTradeableTickSize())
				.setCurrency(instrument.getCurrency());

		List<kaqt.foundation.db.DbAlternateId> altIds = instrument
				.getAlternateIds();

		if (!altIds.isEmpty())
		{
			for (int i = 0; i < altIds.size(); ++i)
			{
				kaqt.foundation.db.DbAlternateId ai = altIds.get(i);
				AlternateId pfAltId = AlternateId.newBuilder()
						.setDescription("").setId(ai.getId())
						.setIdType(ai.getType()).build();
				rvBuilder.addAlternateId(pfAltId);
			}
		}

		return rvBuilder.build();
	}

	public static FuturesInstrumentResponse createFuturesInstrumentResponse(
			FuturesInstrumentRequest.RequestType type,
			List<FuturesInstrument> repos)
	{
		FuturesInstrumentResponse.Builder rvBuilder = FuturesInstrumentResponse
				.newBuilder();
		switch (type)
		{
		case ALL:
			repos.stream().forEach(i -> rvBuilder.addInstruments(i));
			break;
		default:
			break;
		}
		return rvBuilder.build();
	}

	public static FuturesInstrumentRequest createFuturesInstrumentRequst(
			String type)
	{
		FuturesInstrumentRequest.RequestType requestType = FuturesInstrumentRequest.RequestType
				.valueOf(type);

		FuturesInstrumentRequest.Builder rvBuilder = FuturesInstrumentRequest
				.newBuilder().setType(requestType);

		switch (requestType)
		{
		case EXCHANGE:
			rvBuilder.setExchange("CME");
			break;
		default:
			break;
		}

		return rvBuilder.build();
	}
}
