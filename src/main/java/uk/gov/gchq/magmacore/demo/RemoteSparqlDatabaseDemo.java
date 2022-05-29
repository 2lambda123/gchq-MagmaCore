/*
 * Copyright 2021 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package uk.gov.gchq.magmacore.demo;

import org.apache.jena.query.Dataset;

import uk.gov.gchq.magmacore.database.MagmaCoreDatabase;
import uk.gov.gchq.magmacore.database.MagmaCoreRemoteSparqlDatabase;

/**
 * Example use-case scenario for using a {@link MagmaCoreRemoteSparqlDatabase} with a remote service.
 */
public final class RemoteSparqlDatabaseDemo {

    private final String url;

    public RemoteSparqlDatabaseDemo(final String url) {
      this.url = url;
    }

    /**
     * Run the demo.
     */
    public void run(final boolean populate) {
        final MagmaCoreDatabase db;

        if (populate) {
            final Dataset dataset = ExampleDataObjects.buildDataset();
            db = new MagmaCoreRemoteSparqlDatabase(url, dataset);
        } else {
            db = new MagmaCoreRemoteSparqlDatabase(url);
        }

        db.dump(System.out);
    }

}