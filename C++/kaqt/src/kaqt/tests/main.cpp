#include <iostream>
#include <string>

#include <kaqt/providers/tt/symbology_parser.hpp>
#include <kaqt/foundation/factory.hpp>

#include <gtest/gtest.h>
#include <quickfix/Field.h>

enum class BaseMarketDataPublisherType
{
    TEST = 0,
    SIMULATED = 1,
    TT = 2
};

class BaseMarketDataPublisher
{
private:
public:
    BaseMarketDataPublisher() {}
    virtual ~BaseMarketDataPublisher() {}
    virtual void update(int data) = 0;
};

int test_data = 0;

class TestMarketDataPublisher : public BaseMarketDataPublisher {
private:
    int data_;
public:
    TestMarketDataPublisher() { data_ = 0; }
    ~TestMarketDataPublisher() {}
    void update(int data) { test_data = data; }
    static std::unique_ptr<BaseMarketDataPublisher> create()
    {
        return std::unique_ptr<BaseMarketDataPublisher>(new TestMarketDataPublisher());
    }
};


TEST(SymbologyParserTests, ColumnNameTest)
{
	auto sut = kaqt::providers::tt::SymbologyParser::column_name(FIX::FIELD::Symbol);
	ASSERT_EQ(sut, "Symbol");
}

TEST(FactoryTest, UpdateTest)
{
    Factory<BaseMarketDataPublisherType, BaseMarketDataPublisher> publisher_factory;
    publisher_factory.register_type(BaseMarketDataPublisherType::TEST, TestMarketDataPublisher::create);

    auto sut = publisher_factory.create(BaseMarketDataPublisherType::TEST);

    ASSERT_NE(sut, nullptr);
    ASSERT_EQ(test_data, 0);

    sut->update(10);
    ASSERT_EQ(test_data, 10);
}

TEST(FactoryTest, RegisterTest)
{
    Factory<BaseMarketDataPublisherType, BaseMarketDataPublisher> publisher_factory;
    
    auto sut = publisher_factory.create(BaseMarketDataPublisherType::TEST);
    ASSERT_EQ(sut, nullptr);
}


int main(int argc, char** argv)
{
	::testing::InitGoogleTest(&argc, argv);
	return RUN_ALL_TESTS();
}