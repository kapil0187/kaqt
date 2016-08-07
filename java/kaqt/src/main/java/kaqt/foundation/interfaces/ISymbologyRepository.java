package kaqt.foundation.interfaces;

import java.util.List;

import kaqt.providers.protobuf.Symbology.FuturesInstrument;

public interface ISymbologyRepository
{
	List<FuturesInstrument> getAllDefinitions();
}
