#!/bin/sh

protoc -I. -I../proto/  ../proto/symbology.proto --java_out=../java/kaqt/src/main/java --python_out=../python/kaqt/kaqt/providers/protobuf --cpp_out=../C++/kaqt/include/kaqt/providers/protobuf
protoc -I. -I../proto/ ../proto/levelone_quote.proto --java_out=../java/kaqt/src/main/java --python_out=../python/kaqt/kaqt/providers/protobuf --cpp_out=../C++/kaqt/include/kaqt/providers/protobuf

