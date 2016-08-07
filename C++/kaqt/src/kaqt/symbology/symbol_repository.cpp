#include <kaqt/symbology/symbol_repository.hpp>

namespace kaqt
{
	namespace symbology
	{
		std::map<int, kaqt::providers::protobuf::FuturesInstrument> SymbolRepository::repository_;

		kaqt::symbology::FuturesInstrumentClient SymbolRepository::client_;

		void SymbolRepository::initialize(const std::string& server_endpoint)
		{
			client_.start(server_endpoint);
		}

		void SymbolRepository::close()
		{
			client_.stop();
			repository_.clear();
		}

		std::vector<kaqt::providers::protobuf::FuturesInstrument> SymbolRepository::query_all()
		{
			auto inst_ref = client_.instruments();
			for (auto & inst : inst_ref)
			{
				repository_.insert(std::make_pair(inst.id(), inst));
				std::cout << inst.id() << ":- " << inst.description() << std::endl;
			}
			return inst_ref;
		}
	}
}
