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

package  uk.gov.gchq.magmacore.service.transformation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import uk.gov.gchq.hqdm.rdf.iri.HQDM;
import uk.gov.gchq.hqdm.rdf.iri.IRI;
import uk.gov.gchq.hqdm.rdf.iri.RDFS;
import uk.gov.gchq.magmacore.service.MagmaCoreServiceFactory;

/**
 * Check that {@link DbTransformation} works correctly.
 */
public class DbTransformationTest {

    // Dummy IRI for testing.
    private static final String TEST_IRI = "http://example.com/test#test";

    /**
     * Test that multiple DbChangeSets can be applied as a DbTransformation, inverted, and undone.
     *
     * */
    @Test
    public void testApplyAndInvert() {

        final var individualIri = new IRI(TEST_IRI + "1");
        final var personIri = new IRI(TEST_IRI + "2");

        // Create operations to add an object with dummy values.
        final var changes1 = new DbChangeSet(List.of(), List.of(
            new DbCreateOperation(individualIri, RDFS.RDF_TYPE, HQDM.INDIVIDUAL.getIri()),
            new DbCreateOperation(individualIri, HQDM.MEMBER_OF, "class1"),
            new DbCreateOperation(individualIri, HQDM.PART_OF_POSSIBLE_WORLD, "a world")
        ));

        final var changes2 = new DbChangeSet(List.of(), List.of(
            new DbCreateOperation(personIri, RDFS.RDF_TYPE, HQDM.PERSON.getIri()),
            new DbCreateOperation(personIri, HQDM.MEMBER_OF, "class2"),
            new DbCreateOperation(personIri, HQDM.PART_OF_POSSIBLE_WORLD, "another world")
        ));

        final var transformation = new DbTransformation(List.of(
            changes1,
            changes2
        ));

        // Create a database to be updated.
        final var mcService = MagmaCoreServiceFactory.createWithJenaDatabase();

        // Apply the operations.
        mcService.runInTransaction(transformation);

        // Find the thing we just created and assert values are present.
        final var thing1 = mcService.getInTransaction(individualIri);

        assertNotNull(thing1);
        assertTrue(thing1.hasThisValue(RDFS.RDF_TYPE.getIri(), HQDM.INDIVIDUAL.getIri()));
        assertTrue(thing1.hasThisValue(HQDM.MEMBER_OF.getIri(), "class1"));
        assertTrue(thing1.hasThisValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), "a world"));

        final var thing2 = mcService.getInTransaction(personIri);

        assertNotNull(thing2);
        assertTrue(thing2.hasThisValue(RDFS.RDF_TYPE.getIri(), HQDM.PERSON.getIri()));
        assertTrue(thing2.hasThisValue(HQDM.MEMBER_OF.getIri(), "class2"));
        assertTrue(thing2.hasThisValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), "another world"));

        // Invert the operations, apply them in reverse order and assert they are no longer present.
        mcService.runInTransaction(transformation.invert());

        final var thing1FromDb = mcService.getInTransaction(individualIri);
        assertNull(thing1FromDb);

        final var thing2FromDb = mcService.getInTransaction(personIri);
        assertNull(thing2FromDb);
    }
}