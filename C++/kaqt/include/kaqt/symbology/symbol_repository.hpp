#ifndef KAQT_SYMBOLOGY_SYMBOL_REPOSITORY_HPP_
#define KAQT_SYMBOLOGY_SYMBOL_REPOSITORY_HPP_

#include <iostream>
#include <map>

#include <kaqt/foundation/singleton.hpp>
#include <kaqt/symbology/client.hpp>

namespace kaqt
{
	namespace symbology
	{
		class SymbolRepository : public kaqt::foundation::Singleton<SymbolRepository>
		{
		private:
			static kaqt::symbology::FuturesInstrumentClient client_;
			static std::map<int, kaqt::providers::protobuf::FuturesInstrument> repository_;
		public:
			void initialize(const std::string& server_endpoint);
			void close();
			std::vector<kaqt::providers::protobuf::FuturesInstrument> query_all();
		};
	}
}

#endif // KAQT_SYMBOLOGY_SYMBOL_REPOSITORY_HPP_