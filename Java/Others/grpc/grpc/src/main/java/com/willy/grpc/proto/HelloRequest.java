// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: hello_world.proto

package com.willy.grpc.proto;

/**
 * <pre>
 *介面請求入參
 * </pre>
 *
 * Protobuf type {@code proto.HelloRequest}
 */
public  final class HelloRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:proto.HelloRequest)
    HelloRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use HelloRequest.newBuilder() to construct.
  private HelloRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private HelloRequest() {
    request_ = "";
    request1_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private HelloRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            request_ = s;
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            request1_ = s;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.willy.grpc.proto.HelloWorldProto.internal_static_proto_HelloRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.willy.grpc.proto.HelloWorldProto.internal_static_proto_HelloRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.willy.grpc.proto.HelloRequest.class, com.willy.grpc.proto.HelloRequest.Builder.class);
  }

  public static final int REQUEST_FIELD_NUMBER = 1;
  private volatile java.lang.Object request_;
  /**
   * <pre>
   *field number，有多個參數即按順序遞增
   * </pre>
   *
   * <code>string request = 1;</code>
   */
  public java.lang.String getRequest() {
    java.lang.Object ref = request_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      request_ = s;
      return s;
    }
  }
  /**
   * <pre>
   *field number，有多個參數即按順序遞增
   * </pre>
   *
   * <code>string request = 1;</code>
   */
  public com.google.protobuf.ByteString
      getRequestBytes() {
    java.lang.Object ref = request_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      request_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int REQUEST1_FIELD_NUMBER = 2;
  private volatile java.lang.Object request1_;
  /**
   * <code>string request1 = 2;</code>
   */
  public java.lang.String getRequest1() {
    java.lang.Object ref = request1_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      request1_ = s;
      return s;
    }
  }
  /**
   * <code>string request1 = 2;</code>
   */
  public com.google.protobuf.ByteString
      getRequest1Bytes() {
    java.lang.Object ref = request1_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      request1_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getRequestBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, request_);
    }
    if (!getRequest1Bytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, request1_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getRequestBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, request_);
    }
    if (!getRequest1Bytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, request1_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.willy.grpc.proto.HelloRequest)) {
      return super.equals(obj);
    }
    com.willy.grpc.proto.HelloRequest other = (com.willy.grpc.proto.HelloRequest) obj;

    if (!getRequest()
        .equals(other.getRequest())) return false;
    if (!getRequest1()
        .equals(other.getRequest1())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + REQUEST_FIELD_NUMBER;
    hash = (53 * hash) + getRequest().hashCode();
    hash = (37 * hash) + REQUEST1_FIELD_NUMBER;
    hash = (53 * hash) + getRequest1().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.willy.grpc.proto.HelloRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.willy.grpc.proto.HelloRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.willy.grpc.proto.HelloRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.willy.grpc.proto.HelloRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.willy.grpc.proto.HelloRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.willy.grpc.proto.HelloRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.willy.grpc.proto.HelloRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.willy.grpc.proto.HelloRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.willy.grpc.proto.HelloRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.willy.grpc.proto.HelloRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.willy.grpc.proto.HelloRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.willy.grpc.proto.HelloRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.willy.grpc.proto.HelloRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   *介面請求入參
   * </pre>
   *
   * Protobuf type {@code proto.HelloRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:proto.HelloRequest)
      com.willy.grpc.proto.HelloRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.willy.grpc.proto.HelloWorldProto.internal_static_proto_HelloRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.willy.grpc.proto.HelloWorldProto.internal_static_proto_HelloRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.willy.grpc.proto.HelloRequest.class, com.willy.grpc.proto.HelloRequest.Builder.class);
    }

    // Construct using com.willy.grpc.proto.HelloRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      request_ = "";

      request1_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.willy.grpc.proto.HelloWorldProto.internal_static_proto_HelloRequest_descriptor;
    }

    @java.lang.Override
    public com.willy.grpc.proto.HelloRequest getDefaultInstanceForType() {
      return com.willy.grpc.proto.HelloRequest.getDefaultInstance();
    }

    @java.lang.Override
    public com.willy.grpc.proto.HelloRequest build() {
      com.willy.grpc.proto.HelloRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.willy.grpc.proto.HelloRequest buildPartial() {
      com.willy.grpc.proto.HelloRequest result = new com.willy.grpc.proto.HelloRequest(this);
      result.request_ = request_;
      result.request1_ = request1_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.willy.grpc.proto.HelloRequest) {
        return mergeFrom((com.willy.grpc.proto.HelloRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.willy.grpc.proto.HelloRequest other) {
      if (other == com.willy.grpc.proto.HelloRequest.getDefaultInstance()) return this;
      if (!other.getRequest().isEmpty()) {
        request_ = other.request_;
        onChanged();
      }
      if (!other.getRequest1().isEmpty()) {
        request1_ = other.request1_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.willy.grpc.proto.HelloRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.willy.grpc.proto.HelloRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object request_ = "";
    /**
     * <pre>
     *field number，有多個參數即按順序遞增
     * </pre>
     *
     * <code>string request = 1;</code>
     */
    public java.lang.String getRequest() {
      java.lang.Object ref = request_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        request_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     *field number，有多個參數即按順序遞增
     * </pre>
     *
     * <code>string request = 1;</code>
     */
    public com.google.protobuf.ByteString
        getRequestBytes() {
      java.lang.Object ref = request_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        request_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     *field number，有多個參數即按順序遞增
     * </pre>
     *
     * <code>string request = 1;</code>
     */
    public Builder setRequest(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      request_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *field number，有多個參數即按順序遞增
     * </pre>
     *
     * <code>string request = 1;</code>
     */
    public Builder clearRequest() {
      
      request_ = getDefaultInstance().getRequest();
      onChanged();
      return this;
    }
    /**
     * <pre>
     *field number，有多個參數即按順序遞增
     * </pre>
     *
     * <code>string request = 1;</code>
     */
    public Builder setRequestBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      request_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object request1_ = "";
    /**
     * <code>string request1 = 2;</code>
     */
    public java.lang.String getRequest1() {
      java.lang.Object ref = request1_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        request1_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string request1 = 2;</code>
     */
    public com.google.protobuf.ByteString
        getRequest1Bytes() {
      java.lang.Object ref = request1_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        request1_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string request1 = 2;</code>
     */
    public Builder setRequest1(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      request1_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string request1 = 2;</code>
     */
    public Builder clearRequest1() {
      
      request1_ = getDefaultInstance().getRequest1();
      onChanged();
      return this;
    }
    /**
     * <code>string request1 = 2;</code>
     */
    public Builder setRequest1Bytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      request1_ = value;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:proto.HelloRequest)
  }

  // @@protoc_insertion_point(class_scope:proto.HelloRequest)
  private static final com.willy.grpc.proto.HelloRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.willy.grpc.proto.HelloRequest();
  }

  public static com.willy.grpc.proto.HelloRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<HelloRequest>
      PARSER = new com.google.protobuf.AbstractParser<HelloRequest>() {
    @java.lang.Override
    public HelloRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new HelloRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<HelloRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<HelloRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.willy.grpc.proto.HelloRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
