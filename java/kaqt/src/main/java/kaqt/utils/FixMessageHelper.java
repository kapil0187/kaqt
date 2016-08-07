package kaqt.utils;

import java.util.Map;

import com.sun.media.jfxmedia.logging.Logger;

import kaqt.foundation.mktdata.Quote;
import kaqt.providers.protobuf.Symbology.FuturesInstrument;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.MDEntryPx;
import quickfix.field.MDEntryType;
import quickfix.field.MsgType;
import quickfix.field.RawData;
import quickfix.field.SecurityID;
import quickfix.fix42.MarketDataSnapshotFullRefresh;
import quickfix.fix42.MarketDataSnapshotFullRefresh.NoMDEntries;

public class FixMessageHelper
{
	public static final int CONTRACT_SYMBOL_FIELD = 10455;
	public static final int PRICE_DISPLAY_TYPE_FIELD = 16451;
	public static final int TICK_SIZE_FIELD = 16452;
	public static final int POINT_VALUE_FIELD = 16454;
	public static final int EXCHANGE_TICK_SIZE = 16552;
	public static final int EXCHANGE_POINT_VALUE = 16554;
	public static final int PASSWORD_FIELD = 96;
	public static final int REQUEST_TICK_TABLE_FIELD = 17000;

	// TODO: We can use reflection to generate the FIX Field
	public static Message passwordify(Message message, String password)
	{
		MsgType msgType = new MsgType();
		try
		{
			if (message.getHeader().getField(msgType).valueEquals("A"))
			{
				message.setField(new RawData(password));
			}
		}
		catch (FieldNotFound e)
		{
			e.printStackTrace();
		}

		return message;
	}

	public static Quote createQuote(MarketDataSnapshotFullRefresh snapshot,
			Map<String, FuturesInstrument> instdefReference)
			throws FieldNotFound
	{
		// 8=FIX.4.29=19335=V34=53449=KAPILDTS52=20151005-04:01:33.98456=TTDEV13P262=3e8de146-cdb0-487c-a076-b60b120aaf92263=2264=1265=0266=Y146=155=NG48=00A0LX00NGZ167=FUT207=CME267=3269=0269=1269=210=092
		SecurityID securityId = new SecurityID();
		snapshot.get(securityId);

		Quote rv = new Quote();
		rv.setId(instdefReference.get(securityId.getValue()).getId());

		int numEntries = snapshot.getNoMDEntries().getValue();

		if (numEntries > 0)
		{
			System.out.println(String.format("RECIEVED %s ENTRIES", numEntries));
			try
			{
				for (int i = 1; i <= numEntries; ++i)
				{
					NoMDEntries mdGroup = new NoMDEntries();
					snapshot.getGroup(i, mdGroup);
					switch (mdGroup.getMDEntryType().getValue())
					{
					case MDEntryType.BID:
						rv.setBidPrice(mdGroup.getMDEntryPx().getValue());
						rv.setBidQty((int) mdGroup.getMDEntrySize().getValue());
						break;
					case MDEntryType.OFFER:
						rv.setAskPrice(mdGroup.getMDEntryPx().getValue());
						rv.setAskQty((int) mdGroup.getMDEntrySize().getValue());
						break;
					case MDEntryType.TRADE:
						rv.setLastPrice(mdGroup.getMDEntryPx().getValue());
						rv.setLastQty((int) mdGroup.getMDEntrySize().getValue());
						break;

					default:
						break;
					}
					System.out.println("########### PARSED YAYY!! ##############");
				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		rv.setPosixTime(0);

		return rv;
	}
}
