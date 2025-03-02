/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.backward_codecs.store;

import java.io.IOException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.RandomAccessInput;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.LuceneTestCase.Nightly;

@Nightly // N-2 formats are only tested on nightly runs
public abstract class EndiannessReverserTestCase extends LuceneTestCase {

  protected abstract IndexInput getEndiannessReverserInput(
      Directory directory, String name, IOContext context) throws IOException;

  protected abstract IndexOutput getEndiannessReverserOutput(
      Directory directory, String name, IOContext context) throws IOException;

  protected abstract boolean supportSlice();

  public void testReadShort() throws IOException {
    final Directory directory = newDirectory();
    final IndexOutput output;
    if (random().nextBoolean()) {
      output = getEndiannessReverserOutput(directory, "endianness", IOContext.DEFAULT);
    } else {
      output = directory.createOutput("endianness", IOContext.DEFAULT);
    }
    int values = atLeast(30);
    for (int i = 0; i < values; i++) {
      output.writeShort((short) random().nextInt());
    }
    long len = output.getFilePointer();
    output.close();
    {
      IndexInput input = directory.openInput("endianness", IOContext.DEFAULT);
      IndexInput wrapped = getEndiannessReverserInput(directory, "endianness", IOContext.DEFAULT);
      for (int i = 0; i < values; i++) {
        assertEquals(input.readShort(), Short.reverseBytes(wrapped.readShort()));
      }
      input.close();
      wrapped.close();
    }
    if (supportSlice()) {
      {
        IndexInput input = directory.openInput("endianness", IOContext.DEFAULT);
        IndexInput wrapped = getEndiannessReverserInput(directory, "endianness", IOContext.DEFAULT);
        IndexInput slice = wrapped.slice("slice", 0, len);
        for (int i = 0; i < values; i++) {
          assertEquals(input.readShort(), Short.reverseBytes(slice.readShort()));
        }
        slice.close();
        input.close();
        wrapped.close();
      }
      {
        IndexInput input = directory.openInput("endianness", IOContext.DEFAULT);
        RandomAccessInput randomAccessInput = input.randomAccessSlice(0, len);
        IndexInput wrapped = getEndiannessReverserInput(directory, "endianness", IOContext.DEFAULT);
        RandomAccessInput randomAccessWrapped = wrapped.randomAccessSlice(0, len);
        for (int i = 0; i < values; i++) {
          assertEquals(
              randomAccessInput.readShort(2 * i),
              Short.reverseBytes(randomAccessWrapped.readShort(2 * i)));
        }
        input.close();
        wrapped.close();
      }
    }
    directory.close();
  }

  public void testReadInt() throws IOException {
    Directory directory = newDirectory();
    IndexOutput output;
    if (random().nextBoolean()) {
      output = getEndiannessReverserOutput(directory, "endianness", IOContext.DEFAULT);
    } else {
      output = directory.createOutput("endianness", IOContext.DEFAULT);
    }
    int values = atLeast(30);
    for (int i = 0; i < values; i++) {
      output.writeInt(random().nextInt());
    }
    long len = output.getFilePointer();
    output.close();
    {
      IndexInput input = directory.openInput("endianness", IOContext.DEFAULT);
      IndexInput wrapped = getEndiannessReverserInput(directory, "endianness", IOContext.DEFAULT);
      for (int i = 0; i < values; i++) {
        assertEquals(input.readInt(), Integer.reverseBytes(wrapped.readInt()));
      }
      input.close();
      wrapped.close();
    }
    if (supportSlice()) {
      {
        IndexInput input = directory.openInput("endianness", IOContext.DEFAULT);
        IndexInput wrapped = getEndiannessReverserInput(directory, "endianness", IOContext.DEFAULT);
        IndexInput slice = wrapped.slice("slice", 0, len);
        for (int i = 0; i < values; i++) {
          assertEquals(input.readInt(), Integer.reverseBytes(slice.readInt()));
        }
        slice.close();
        input.close();
        wrapped.close();
      }
      {
        IndexInput input = directory.openInput("endianness", IOContext.DEFAULT);
        RandomAccessInput randomAccessInput = input.randomAccessSlice(0, len);
        IndexInput wrapped = getEndiannessReverserInput(directory, "endianness", IOContext.DEFAULT);
        RandomAccessInput randomAccessWrapped = wrapped.randomAccessSlice(0, len);
        for (int i = 0; i < values; i++) {
          assertEquals(
              randomAccessInput.readInt(4 * i),
              Integer.reverseBytes(randomAccessWrapped.readInt(4 * i)));
        }
        input.close();
        wrapped.close();
      }
    }
    directory.close();
  }

  public void testReadLong() throws IOException {
    Directory directory = newDirectory();
    IndexOutput output;
    if (random().nextBoolean()) {
      output = getEndiannessReverserOutput(directory, "endianness", IOContext.DEFAULT);
    } else {
      output = directory.createOutput("endianness", IOContext.DEFAULT);
    }
    int values = atLeast(30);
    for (int i = 0; i < values; i++) {
      output.writeLong(random().nextLong());
    }
    long len = output.getFilePointer();
    output.close();
    {
      IndexInput input = directory.openInput("endianness", IOContext.DEFAULT);
      IndexInput wrapped = getEndiannessReverserInput(directory, "endianness", IOContext.DEFAULT);
      for (int i = 0; i < values; i++) {
        assertEquals(input.readLong(), Long.reverseBytes(wrapped.readLong()));
      }
      input.close();
      wrapped.close();
    }
    if (supportSlice()) {
      {
        IndexInput input = directory.openInput("endianness", IOContext.DEFAULT);
        IndexInput wrapped = getEndiannessReverserInput(directory, "endianness", IOContext.DEFAULT);
        IndexInput slice = wrapped.slice("slice", 0, len);
        for (int i = 0; i < values; i++) {
          assertEquals(input.readLong(), Long.reverseBytes(slice.readLong()));
        }
        slice.close();
        input.close();
        wrapped.close();
      }
      {
        IndexInput input = directory.openInput("endianness", IOContext.DEFAULT);
        RandomAccessInput randomAccessInput = input.randomAccessSlice(0, len);
        IndexInput wrapped = getEndiannessReverserInput(directory, "endianness", IOContext.DEFAULT);
        RandomAccessInput randomAccessWrapped = wrapped.randomAccessSlice(0, len);
        for (int i = 0; i < values; i++) {
          assertEquals(
              randomAccessInput.readLong(8 * i),
              Long.reverseBytes(randomAccessWrapped.readLong(8 * i)));
        }
        input.close();
        wrapped.close();
      }
    }
    directory.close();
  }
}
