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
package org.apache.lucene.backward_codecs.lucene50;

import com.carrotsearch.randomizedtesting.generators.RandomPicks;
import org.apache.lucene.backward_codecs.lucene50.Lucene50StoredFieldsFormat.Mode;
import org.apache.lucene.backward_codecs.lucene86.Lucene86RWCodec;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.BaseStoredFieldsFormatTestCase;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase.Nightly;

@Nightly // N-2 formats are only tested on nightly runs
public class TestLucene50StoredFieldsFormatHighCompression extends BaseStoredFieldsFormatTestCase {
  @Override
  protected Codec getCodec() {
    return new Lucene86RWCodec(Mode.BEST_COMPRESSION);
  }

  /**
   * Change compression params (leaving it the same for old segments) and tests that nothing breaks.
   */
  public void testMixedCompressions() throws Exception {
    Directory dir = newDirectory();
    for (int i = 0; i < 10; i++) {
      IndexWriterConfig iwc = newIndexWriterConfig();
      iwc.setCodec(new Lucene86RWCodec(RandomPicks.randomFrom(random(), Mode.values())));
      IndexWriter iw = new IndexWriter(dir, newIndexWriterConfig());
      Document doc = new Document();
      doc.add(new StoredField("field1", "value1"));
      doc.add(new StoredField("field2", "value2"));
      iw.addDocument(doc);
      if (random().nextInt(4) == 0) {
        iw.forceMerge(1);
      }
      iw.commit();
      iw.close();
    }

    DirectoryReader ir = DirectoryReader.open(dir);
    assertEquals(10, ir.numDocs());
    for (int i = 0; i < 10; i++) {
      Document doc = ir.document(i);
      assertEquals("value1", doc.get("field1"));
      assertEquals("value2", doc.get("field2"));
    }
    ir.close();
    // checkindex
    dir.close();
  }

  public void testInvalidOptions() {
    expectThrows(
        NullPointerException.class,
        () -> {
          new Lucene86RWCodec(null);
        });

    expectThrows(
        NullPointerException.class,
        () -> {
          new Lucene50StoredFieldsFormat(null);
        });
  }
}
