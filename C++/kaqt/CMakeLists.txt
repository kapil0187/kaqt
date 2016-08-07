cmake_minimum_required(VERSION 3.1)
project(kaqt)

set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${CMAKE_SOURCE_DIR}/cmake/Modules/)

set(CMAKE_VERBOSE_MAKEFILE true)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
    include/kaqt/foundation/singleton.hpp
    include/kaqt/foundation/id_generator.hpp
    include/kaqt/providers/quickfix/fix42/message_factory.hpp
    include/kaqt/providers/tt/constants.hpp
    include/kaqt/providers/tt/fixadaptor_client.hpp
    include/kaqt/providers/protobuf/levelone_quote.pb.h
    include/kaqt/providers/protobuf/symbology.pb.h
    include/kaqt/providers/protobuf/levelone_quote.pb.cc
    include/kaqt/providers/protobuf/symbology.pb.cc
    include/kaqt/symbology/symbol_factory.hpp
    include/kaqt/symbology/symbol_repository.hpp
    include/kaqt/symbology/client.hpp
    include/kaqt/foundation/csv_writer.hpp
    include/kaqt/providers/quickfix/log_reader.hpp
    include/kaqt/providers/tt/symbology_parser.hpp
    src/kaqt/foundation/id_generator.cpp
    src/kaqt/providers/quickfix/fix42/message_factory.cpp
    src/kaqt/providers/tt/constants.cpp
    src/kaqt/providers/tt/fixadaptor_client.cpp
    src/kaqt/symbology/symbol_factory.cpp
    src/kaqt/symbology/symbol_repository.cpp
    src/kaqt/symbology/client.cpp
    src/kaqt/foundation/csv_writer.cpp
    src/kaqt/providers/quickfix/log_reader.cpp
    src/kaqt/providers/tt/symbology_parser.cpp)

FILE(GLOB SPDLOG_HEADERS ./include/spdlog/*.h ./include/spdlog/sinks/*.h ./include/spdlog/details/*.h)

message("Finding Boost libraries...")

set(BOOST_ROOT /usr/local)
find_package(Boost 1.58 REQUIRED COMPONENTS program_options system filesystem regex)

if(Boost_FOUND)
    message("Boost Found")
    include_directories(${Boost_INCLUDE_DIRS})
endif()

message("Finding QuickFix libraries...")
find_package(QuickFIX REQUIRED)

if(QuickFIX_FOUND)
    message("Quickfix Found")
    include_directories(${QUICKFIX_INCLUDE_DIRS})
endif()

message("Finding Protobuf libraries...")
set(PROTOBUF_SRC_ROOT_FOLDER /usr/local)
find_package(Protobuf REQUIRED)
include_directories(PROTOBUF_INCLUDE_DIR)

message("Finding ZeroMQ libraries...")
set(ZeroMQ_ROOT_DIR /usr/local)
find_package(ZeroMQ REQUIRED)
include_directories(ZeroMQ_INCLUDE_DIRS)

include_directories(include)

add_subdirectory(kaqt.apps.tt)
add_subdirectory(kaqt.apps.symbology.ca)
add_subdirectory(kaqt.tests)
add_subdirectory(kaqt.apps.tt.symbologyparser)

add_library(kaqt STATIC ${SOURCE_FILES})

install(TARGETS kaqt DESTINATION lib)
install(FILES ${SOURCE_FILES} DESTINATION include)
target_link_libraries(kaqt ${QUICKFIX_LIBRARIES} ${Boost_LIBRARIES} ${PROTOBUF_LIBRARIES} ${ZeroMQ_LIBRARIES})

add_test(kaqt.tests kaqt)
