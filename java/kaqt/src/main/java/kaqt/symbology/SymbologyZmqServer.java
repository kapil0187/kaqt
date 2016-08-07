package kaqt.symbology;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException;

import com.google.protobuf.InvalidProtocolBufferException;

import kaqt.foundation.connections.ContextSingleton;
import kaqt.foundation.db.DbFuturesInstrument;
import kaqt.foundation.db.MongoClientConnection;
import kaqt.providers.protobuf.Symbology.FuturesInstrument;
import kaqt.providers.protobuf.Symbology.FuturesInstrumentRequest;
import kaqt.providers.protobuf.Symbology.FuturesInstrumentResponse;
import kaqt.utils.ProtoBufHelper;

public class SymbologyZmqServer extends MongoClientConnection
{
	private static Logger logger = Logger.getLogger(SymbologyZmqServer.class);

	private List<FuturesInstrument> instruments;
	private Thread serverThread;
	private Context context;
	private Socket responder;
	private String bindAddress;
	private boolean terminated = false;

	public SymbologyZmqServer(String configFile, int serverPort)
	{
		super(configFile);
		this.bindAddress = String.format("tcp://*:%s", serverPort);
	}

	public void start()
	{
		serverThread = new Thread(this::serverProcessor);
		serverThread.start();
	}

	private void serverProcessor()
	{
		logger.info("LOADING ALL INSTRUMENTS FROM DATABASE");

		instruments = new ArrayList<FuturesInstrument>();
		List<DbFuturesInstrument> queryResults = super.mongoOperations
				.findAll(DbFuturesInstrument.class);

		if (queryResults != null && !queryResults.isEmpty())
		{
			queryResults.stream().forEach(
					i -> instruments.add(ProtoBufHelper
							.createFuturesInstrument(i)));
		}
		else
		{
			logger.warn("MONGO DB QUERY RESULTED IN 0 SEARCH RESULTS");
		}

		logger.info("STARTING ZMQ SERVER FOR INSTRUMENTS DEFINITION");

		this.context = ContextSingleton.getInstance();
		this.responder = context.socket(ZMQ.REP);

		responder.bind(this.bindAddress);

		logger.info(String.format("STARTED SERVER WITH BIND ADDRESS: %s",
				this.bindAddress));

		try
		{
			while (!terminated)
			{
				byte[] requestBytes = responder.recv(0);

				logger.info("RECEIVED REQUEST");

				FuturesInstrumentRequest request = null;
				try
				{
					request = FuturesInstrumentRequest.parseFrom(requestBytes);
				}
				catch (InvalidProtocolBufferException e)
				{
					logger.error("FAILED TO PARSE FUTURES INSTRUMENT REQUEST");
				}

				if (request != null)
				{
					FuturesInstrumentResponse response = ProtoBufHelper
							.createFuturesInstrumentResponse(request.getType(),
									this.instruments);
					responder.send(response.toByteArray());
					request = null;
				}
				else
				{
					logger.error("FAILED TO SEND RESPONSE FOR FUTURES INSTRUMENT REQUEST");
				}
			}
		}
		catch (ZMQException e)
		{
			logger.error("TERMINATING THE SYMBOLOGY SERVER");
		}
	}

	public void stop()
	{
		terminated = true;
		
		responder.unbind(this.bindAddress);
		responder.close();
		context.term();

		logger.info("CLOSING CONTEXT AND SOCKET");

		// context.term();
		try
		{
			logger.info("JOINING SERVER THREAD");
			serverThread.join();
		}
		catch (InterruptedException e)
		{
			logger.info("COULD NOT JOIN SERVER THREAD");
		}

		instruments.clear();

		logger.info("CLEARED INSTRUMENTS CACHE");
	}
}
