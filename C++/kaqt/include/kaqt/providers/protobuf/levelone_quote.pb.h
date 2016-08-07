// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: levelone_quote.proto

#ifndef PROTOBUF_levelone_5fquote_2eproto__INCLUDED
#define PROTOBUF_levelone_5fquote_2eproto__INCLUDED

#include <string>

#include <google/protobuf/stubs/common.h>

#if GOOGLE_PROTOBUF_VERSION < 2006000
#error This file was generated by a newer version of protoc which is
#error incompatible with your Protocol Buffer headers.  Please update
#error your headers.
#endif
#if 2006001 < GOOGLE_PROTOBUF_MIN_PROTOC_VERSION
#error This file was generated by an older version of protoc which is
#error incompatible with your Protocol Buffer headers.  Please
#error regenerate this file with a newer version of protoc.
#endif

#include <google/protobuf/generated_message_util.h>
#include <google/protobuf/message.h>
#include <google/protobuf/repeated_field.h>
#include <google/protobuf/extension_set.h>
#include <google/protobuf/unknown_field_set.h>
// @@protoc_insertion_point(includes)

namespace kaqt {
namespace providers {
namespace protobuf {

// Internal implementation detail -- do not call these.
void  protobuf_AddDesc_levelone_5fquote_2eproto();
void protobuf_AssignDesc_levelone_5fquote_2eproto();
void protobuf_ShutdownFile_levelone_5fquote_2eproto();

class LevelOneQuote;
class HLOC;

// ===================================================================

class LevelOneQuote : public ::google::protobuf::Message {
 public:
  LevelOneQuote();
  virtual ~LevelOneQuote();

  LevelOneQuote(const LevelOneQuote& from);

  inline LevelOneQuote& operator=(const LevelOneQuote& from) {
    CopyFrom(from);
    return *this;
  }

  inline const ::google::protobuf::UnknownFieldSet& unknown_fields() const {
    return _unknown_fields_;
  }

  inline ::google::protobuf::UnknownFieldSet* mutable_unknown_fields() {
    return &_unknown_fields_;
  }

  static const ::google::protobuf::Descriptor* descriptor();
  static const LevelOneQuote& default_instance();

  void Swap(LevelOneQuote* other);

  // implements Message ----------------------------------------------

  LevelOneQuote* New() const;
  void CopyFrom(const ::google::protobuf::Message& from);
  void MergeFrom(const ::google::protobuf::Message& from);
  void CopyFrom(const LevelOneQuote& from);
  void MergeFrom(const LevelOneQuote& from);
  void Clear();
  bool IsInitialized() const;

  int ByteSize() const;
  bool MergePartialFromCodedStream(
      ::google::protobuf::io::CodedInputStream* input);
  void SerializeWithCachedSizes(
      ::google::protobuf::io::CodedOutputStream* output) const;
  ::google::protobuf::uint8* SerializeWithCachedSizesToArray(::google::protobuf::uint8* output) const;
  int GetCachedSize() const { return _cached_size_; }
  private:
  void SharedCtor();
  void SharedDtor();
  void SetCachedSize(int size) const;
  public:
  ::google::protobuf::Metadata GetMetadata() const;

  // nested types ----------------------------------------------------

  // accessors -------------------------------------------------------

  // required int32 instrument_id = 1;
  inline bool has_instrument_id() const;
  inline void clear_instrument_id();
  static const int kInstrumentIdFieldNumber = 1;
  inline ::google::protobuf::int32 instrument_id() const;
  inline void set_instrument_id(::google::protobuf::int32 value);

  // required double bid = 2;
  inline bool has_bid() const;
  inline void clear_bid();
  static const int kBidFieldNumber = 2;
  inline double bid() const;
  inline void set_bid(double value);

  // required double ask = 3;
  inline bool has_ask() const;
  inline void clear_ask();
  static const int kAskFieldNumber = 3;
  inline double ask() const;
  inline void set_ask(double value);

  // required double last = 4;
  inline bool has_last() const;
  inline void clear_last();
  static const int kLastFieldNumber = 4;
  inline double last() const;
  inline void set_last(double value);

  // required int32 bid_qty = 5;
  inline bool has_bid_qty() const;
  inline void clear_bid_qty();
  static const int kBidQtyFieldNumber = 5;
  inline ::google::protobuf::int32 bid_qty() const;
  inline void set_bid_qty(::google::protobuf::int32 value);

  // required int32 ask_qty = 6;
  inline bool has_ask_qty() const;
  inline void clear_ask_qty();
  static const int kAskQtyFieldNumber = 6;
  inline ::google::protobuf::int32 ask_qty() const;
  inline void set_ask_qty(::google::protobuf::int32 value);

  // required int32 last_qty = 7;
  inline bool has_last_qty() const;
  inline void clear_last_qty();
  static const int kLastQtyFieldNumber = 7;
  inline ::google::protobuf::int32 last_qty() const;
  inline void set_last_qty(::google::protobuf::int32 value);

  // required int64 posix_time = 8;
  inline bool has_posix_time() const;
  inline void clear_posix_time();
  static const int kPosixTimeFieldNumber = 8;
  inline ::google::protobuf::int64 posix_time() const;
  inline void set_posix_time(::google::protobuf::int64 value);

  // @@protoc_insertion_point(class_scope:kaqt.providers.protobuf.LevelOneQuote)
 private:
  inline void set_has_instrument_id();
  inline void clear_has_instrument_id();
  inline void set_has_bid();
  inline void clear_has_bid();
  inline void set_has_ask();
  inline void clear_has_ask();
  inline void set_has_last();
  inline void clear_has_last();
  inline void set_has_bid_qty();
  inline void clear_has_bid_qty();
  inline void set_has_ask_qty();
  inline void clear_has_ask_qty();
  inline void set_has_last_qty();
  inline void clear_has_last_qty();
  inline void set_has_posix_time();
  inline void clear_has_posix_time();

  ::google::protobuf::UnknownFieldSet _unknown_fields_;

  ::google::protobuf::uint32 _has_bits_[1];
  mutable int _cached_size_;
  double bid_;
  double ask_;
  ::google::protobuf::int32 instrument_id_;
  ::google::protobuf::int32 bid_qty_;
  double last_;
  ::google::protobuf::int32 ask_qty_;
  ::google::protobuf::int32 last_qty_;
  ::google::protobuf::int64 posix_time_;
  friend void  protobuf_AddDesc_levelone_5fquote_2eproto();
  friend void protobuf_AssignDesc_levelone_5fquote_2eproto();
  friend void protobuf_ShutdownFile_levelone_5fquote_2eproto();

  void InitAsDefaultInstance();
  static LevelOneQuote* default_instance_;
};
// -------------------------------------------------------------------

class HLOC : public ::google::protobuf::Message {
 public:
  HLOC();
  virtual ~HLOC();

  HLOC(const HLOC& from);

  inline HLOC& operator=(const HLOC& from) {
    CopyFrom(from);
    return *this;
  }

  inline const ::google::protobuf::UnknownFieldSet& unknown_fields() const {
    return _unknown_fields_;
  }

  inline ::google::protobuf::UnknownFieldSet* mutable_unknown_fields() {
    return &_unknown_fields_;
  }

  static const ::google::protobuf::Descriptor* descriptor();
  static const HLOC& default_instance();

  void Swap(HLOC* other);

  // implements Message ----------------------------------------------

  HLOC* New() const;
  void CopyFrom(const ::google::protobuf::Message& from);
  void MergeFrom(const ::google::protobuf::Message& from);
  void CopyFrom(const HLOC& from);
  void MergeFrom(const HLOC& from);
  void Clear();
  bool IsInitialized() const;

  int ByteSize() const;
  bool MergePartialFromCodedStream(
      ::google::protobuf::io::CodedInputStream* input);
  void SerializeWithCachedSizes(
      ::google::protobuf::io::CodedOutputStream* output) const;
  ::google::protobuf::uint8* SerializeWithCachedSizesToArray(::google::protobuf::uint8* output) const;
  int GetCachedSize() const { return _cached_size_; }
  private:
  void SharedCtor();
  void SharedDtor();
  void SetCachedSize(int size) const;
  public:
  ::google::protobuf::Metadata GetMetadata() const;

  // nested types ----------------------------------------------------

  // accessors -------------------------------------------------------

  // required int32 instrument_id = 1;
  inline bool has_instrument_id() const;
  inline void clear_instrument_id();
  static const int kInstrumentIdFieldNumber = 1;
  inline ::google::protobuf::int32 instrument_id() const;
  inline void set_instrument_id(::google::protobuf::int32 value);

  // required double open = 2;
  inline bool has_open() const;
  inline void clear_open();
  static const int kOpenFieldNumber = 2;
  inline double open() const;
  inline void set_open(double value);

  // required double high = 3;
  inline bool has_high() const;
  inline void clear_high();
  static const int kHighFieldNumber = 3;
  inline double high() const;
  inline void set_high(double value);

  // required double low = 4;
  inline bool has_low() const;
  inline void clear_low();
  static const int kLowFieldNumber = 4;
  inline double low() const;
  inline void set_low(double value);

  // required double close = 5;
  inline bool has_close() const;
  inline void clear_close();
  static const int kCloseFieldNumber = 5;
  inline double close() const;
  inline void set_close(double value);

  // required int64 posix_time = 6;
  inline bool has_posix_time() const;
  inline void clear_posix_time();
  static const int kPosixTimeFieldNumber = 6;
  inline ::google::protobuf::int64 posix_time() const;
  inline void set_posix_time(::google::protobuf::int64 value);

  // @@protoc_insertion_point(class_scope:kaqt.providers.protobuf.HLOC)
 private:
  inline void set_has_instrument_id();
  inline void clear_has_instrument_id();
  inline void set_has_open();
  inline void clear_has_open();
  inline void set_has_high();
  inline void clear_has_high();
  inline void set_has_low();
  inline void clear_has_low();
  inline void set_has_close();
  inline void clear_has_close();
  inline void set_has_posix_time();
  inline void clear_has_posix_time();

  ::google::protobuf::UnknownFieldSet _unknown_fields_;

  ::google::protobuf::uint32 _has_bits_[1];
  mutable int _cached_size_;
  double open_;
  double high_;
  double low_;
  double close_;
  ::google::protobuf::int64 posix_time_;
  ::google::protobuf::int32 instrument_id_;
  friend void  protobuf_AddDesc_levelone_5fquote_2eproto();
  friend void protobuf_AssignDesc_levelone_5fquote_2eproto();
  friend void protobuf_ShutdownFile_levelone_5fquote_2eproto();

  void InitAsDefaultInstance();
  static HLOC* default_instance_;
};
// ===================================================================


// ===================================================================

// LevelOneQuote

// required int32 instrument_id = 1;
inline bool LevelOneQuote::has_instrument_id() const {
  return (_has_bits_[0] & 0x00000001u) != 0;
}
inline void LevelOneQuote::set_has_instrument_id() {
  _has_bits_[0] |= 0x00000001u;
}
inline void LevelOneQuote::clear_has_instrument_id() {
  _has_bits_[0] &= ~0x00000001u;
}
inline void LevelOneQuote::clear_instrument_id() {
  instrument_id_ = 0;
  clear_has_instrument_id();
}
inline ::google::protobuf::int32 LevelOneQuote::instrument_id() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.LevelOneQuote.instrument_id)
  return instrument_id_;
}
inline void LevelOneQuote::set_instrument_id(::google::protobuf::int32 value) {
  set_has_instrument_id();
  instrument_id_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.LevelOneQuote.instrument_id)
}

// required double bid = 2;
inline bool LevelOneQuote::has_bid() const {
  return (_has_bits_[0] & 0x00000002u) != 0;
}
inline void LevelOneQuote::set_has_bid() {
  _has_bits_[0] |= 0x00000002u;
}
inline void LevelOneQuote::clear_has_bid() {
  _has_bits_[0] &= ~0x00000002u;
}
inline void LevelOneQuote::clear_bid() {
  bid_ = 0;
  clear_has_bid();
}
inline double LevelOneQuote::bid() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.LevelOneQuote.bid)
  return bid_;
}
inline void LevelOneQuote::set_bid(double value) {
  set_has_bid();
  bid_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.LevelOneQuote.bid)
}

// required double ask = 3;
inline bool LevelOneQuote::has_ask() const {
  return (_has_bits_[0] & 0x00000004u) != 0;
}
inline void LevelOneQuote::set_has_ask() {
  _has_bits_[0] |= 0x00000004u;
}
inline void LevelOneQuote::clear_has_ask() {
  _has_bits_[0] &= ~0x00000004u;
}
inline void LevelOneQuote::clear_ask() {
  ask_ = 0;
  clear_has_ask();
}
inline double LevelOneQuote::ask() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.LevelOneQuote.ask)
  return ask_;
}
inline void LevelOneQuote::set_ask(double value) {
  set_has_ask();
  ask_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.LevelOneQuote.ask)
}

// required double last = 4;
inline bool LevelOneQuote::has_last() const {
  return (_has_bits_[0] & 0x00000008u) != 0;
}
inline void LevelOneQuote::set_has_last() {
  _has_bits_[0] |= 0x00000008u;
}
inline void LevelOneQuote::clear_has_last() {
  _has_bits_[0] &= ~0x00000008u;
}
inline void LevelOneQuote::clear_last() {
  last_ = 0;
  clear_has_last();
}
inline double LevelOneQuote::last() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.LevelOneQuote.last)
  return last_;
}
inline void LevelOneQuote::set_last(double value) {
  set_has_last();
  last_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.LevelOneQuote.last)
}

// required int32 bid_qty = 5;
inline bool LevelOneQuote::has_bid_qty() const {
  return (_has_bits_[0] & 0x00000010u) != 0;
}
inline void LevelOneQuote::set_has_bid_qty() {
  _has_bits_[0] |= 0x00000010u;
}
inline void LevelOneQuote::clear_has_bid_qty() {
  _has_bits_[0] &= ~0x00000010u;
}
inline void LevelOneQuote::clear_bid_qty() {
  bid_qty_ = 0;
  clear_has_bid_qty();
}
inline ::google::protobuf::int32 LevelOneQuote::bid_qty() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.LevelOneQuote.bid_qty)
  return bid_qty_;
}
inline void LevelOneQuote::set_bid_qty(::google::protobuf::int32 value) {
  set_has_bid_qty();
  bid_qty_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.LevelOneQuote.bid_qty)
}

// required int32 ask_qty = 6;
inline bool LevelOneQuote::has_ask_qty() const {
  return (_has_bits_[0] & 0x00000020u) != 0;
}
inline void LevelOneQuote::set_has_ask_qty() {
  _has_bits_[0] |= 0x00000020u;
}
inline void LevelOneQuote::clear_has_ask_qty() {
  _has_bits_[0] &= ~0x00000020u;
}
inline void LevelOneQuote::clear_ask_qty() {
  ask_qty_ = 0;
  clear_has_ask_qty();
}
inline ::google::protobuf::int32 LevelOneQuote::ask_qty() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.LevelOneQuote.ask_qty)
  return ask_qty_;
}
inline void LevelOneQuote::set_ask_qty(::google::protobuf::int32 value) {
  set_has_ask_qty();
  ask_qty_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.LevelOneQuote.ask_qty)
}

// required int32 last_qty = 7;
inline bool LevelOneQuote::has_last_qty() const {
  return (_has_bits_[0] & 0x00000040u) != 0;
}
inline void LevelOneQuote::set_has_last_qty() {
  _has_bits_[0] |= 0x00000040u;
}
inline void LevelOneQuote::clear_has_last_qty() {
  _has_bits_[0] &= ~0x00000040u;
}
inline void LevelOneQuote::clear_last_qty() {
  last_qty_ = 0;
  clear_has_last_qty();
}
inline ::google::protobuf::int32 LevelOneQuote::last_qty() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.LevelOneQuote.last_qty)
  return last_qty_;
}
inline void LevelOneQuote::set_last_qty(::google::protobuf::int32 value) {
  set_has_last_qty();
  last_qty_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.LevelOneQuote.last_qty)
}

// required int64 posix_time = 8;
inline bool LevelOneQuote::has_posix_time() const {
  return (_has_bits_[0] & 0x00000080u) != 0;
}
inline void LevelOneQuote::set_has_posix_time() {
  _has_bits_[0] |= 0x00000080u;
}
inline void LevelOneQuote::clear_has_posix_time() {
  _has_bits_[0] &= ~0x00000080u;
}
inline void LevelOneQuote::clear_posix_time() {
  posix_time_ = GOOGLE_LONGLONG(0);
  clear_has_posix_time();
}
inline ::google::protobuf::int64 LevelOneQuote::posix_time() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.LevelOneQuote.posix_time)
  return posix_time_;
}
inline void LevelOneQuote::set_posix_time(::google::protobuf::int64 value) {
  set_has_posix_time();
  posix_time_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.LevelOneQuote.posix_time)
}

// -------------------------------------------------------------------

// HLOC

// required int32 instrument_id = 1;
inline bool HLOC::has_instrument_id() const {
  return (_has_bits_[0] & 0x00000001u) != 0;
}
inline void HLOC::set_has_instrument_id() {
  _has_bits_[0] |= 0x00000001u;
}
inline void HLOC::clear_has_instrument_id() {
  _has_bits_[0] &= ~0x00000001u;
}
inline void HLOC::clear_instrument_id() {
  instrument_id_ = 0;
  clear_has_instrument_id();
}
inline ::google::protobuf::int32 HLOC::instrument_id() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.HLOC.instrument_id)
  return instrument_id_;
}
inline void HLOC::set_instrument_id(::google::protobuf::int32 value) {
  set_has_instrument_id();
  instrument_id_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.HLOC.instrument_id)
}

// required double open = 2;
inline bool HLOC::has_open() const {
  return (_has_bits_[0] & 0x00000002u) != 0;
}
inline void HLOC::set_has_open() {
  _has_bits_[0] |= 0x00000002u;
}
inline void HLOC::clear_has_open() {
  _has_bits_[0] &= ~0x00000002u;
}
inline void HLOC::clear_open() {
  open_ = 0;
  clear_has_open();
}
inline double HLOC::open() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.HLOC.open)
  return open_;
}
inline void HLOC::set_open(double value) {
  set_has_open();
  open_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.HLOC.open)
}

// required double high = 3;
inline bool HLOC::has_high() const {
  return (_has_bits_[0] & 0x00000004u) != 0;
}
inline void HLOC::set_has_high() {
  _has_bits_[0] |= 0x00000004u;
}
inline void HLOC::clear_has_high() {
  _has_bits_[0] &= ~0x00000004u;
}
inline void HLOC::clear_high() {
  high_ = 0;
  clear_has_high();
}
inline double HLOC::high() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.HLOC.high)
  return high_;
}
inline void HLOC::set_high(double value) {
  set_has_high();
  high_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.HLOC.high)
}

// required double low = 4;
inline bool HLOC::has_low() const {
  return (_has_bits_[0] & 0x00000008u) != 0;
}
inline void HLOC::set_has_low() {
  _has_bits_[0] |= 0x00000008u;
}
inline void HLOC::clear_has_low() {
  _has_bits_[0] &= ~0x00000008u;
}
inline void HLOC::clear_low() {
  low_ = 0;
  clear_has_low();
}
inline double HLOC::low() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.HLOC.low)
  return low_;
}
inline void HLOC::set_low(double value) {
  set_has_low();
  low_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.HLOC.low)
}

// required double close = 5;
inline bool HLOC::has_close() const {
  return (_has_bits_[0] & 0x00000010u) != 0;
}
inline void HLOC::set_has_close() {
  _has_bits_[0] |= 0x00000010u;
}
inline void HLOC::clear_has_close() {
  _has_bits_[0] &= ~0x00000010u;
}
inline void HLOC::clear_close() {
  close_ = 0;
  clear_has_close();
}
inline double HLOC::close() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.HLOC.close)
  return close_;
}
inline void HLOC::set_close(double value) {
  set_has_close();
  close_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.HLOC.close)
}

// required int64 posix_time = 6;
inline bool HLOC::has_posix_time() const {
  return (_has_bits_[0] & 0x00000020u) != 0;
}
inline void HLOC::set_has_posix_time() {
  _has_bits_[0] |= 0x00000020u;
}
inline void HLOC::clear_has_posix_time() {
  _has_bits_[0] &= ~0x00000020u;
}
inline void HLOC::clear_posix_time() {
  posix_time_ = GOOGLE_LONGLONG(0);
  clear_has_posix_time();
}
inline ::google::protobuf::int64 HLOC::posix_time() const {
  // @@protoc_insertion_point(field_get:kaqt.providers.protobuf.HLOC.posix_time)
  return posix_time_;
}
inline void HLOC::set_posix_time(::google::protobuf::int64 value) {
  set_has_posix_time();
  posix_time_ = value;
  // @@protoc_insertion_point(field_set:kaqt.providers.protobuf.HLOC.posix_time)
}


// @@protoc_insertion_point(namespace_scope)

}  // namespace protobuf
}  // namespace providers
}  // namespace kaqt

#ifndef SWIG
namespace google {
namespace protobuf {


}  // namespace google
}  // namespace protobuf
#endif  // SWIG

// @@protoc_insertion_point(global_scope)

#endif  // PROTOBUF_levelone_5fquote_2eproto__INCLUDED