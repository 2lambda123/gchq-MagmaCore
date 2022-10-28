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

package uk.gov.gchq.magmacore.hqdm.rdfbuilders;

import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.HAS_SUPERCLASS;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.MEMBER_OF;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.MEMBER__OF;

import uk.gov.gchq.magmacore.hqdm.exception.HqdmException;
import uk.gov.gchq.magmacore.hqdm.model.Class;
import uk.gov.gchq.magmacore.hqdm.model.ClassOfAbstractObject;
import uk.gov.gchq.magmacore.hqdm.model.ClassOfClass;
import uk.gov.gchq.magmacore.hqdm.rdf.iri.IRI;
import uk.gov.gchq.magmacore.hqdm.rdfservices.RdfClassServices;

/**
 * Builder for constructing instances of ClassOfAbstractObject.
 */
public class ClassOfAbstractObjectBuilder {

    private final ClassOfAbstractObject classOfAbstractObject;

    /**
     * Constructs a Builder for a new ClassOfAbstractObject.
     *
     * @param iri IRI of the ClassOfAbstractObject.
     */
    public ClassOfAbstractObjectBuilder(final IRI iri) {
        classOfAbstractObject = RdfClassServices.createClassOfAbstractObject(iri.getIri());
    }

    /**
     * A relationship type where each {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.IRI.HQDM#MEMBER_OF} the
     * {@link Class} is a {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.IRI.HQDM#MEMBER_OF} the superclass.
     *
     * @param clazz The Class.
     * @return This builder.
     */
    public final ClassOfAbstractObjectBuilder has_Superclass(final Class clazz) {
        classOfAbstractObject.addValue(HAS_SUPERCLASS, new IRI(clazz.getId()));
        return this;
    }

    /**
     * A relationship type where a {@link uk.gov.gchq.magmacore.hqdm.model.Thing} may be a member of one
     * or more {@link Class}.
     *
     * @param clazz The Class.
     * @return This builder.
     */
    public final ClassOfAbstractObjectBuilder member__Of(final Class clazz) {
        classOfAbstractObject.addValue(MEMBER__OF, new IRI(clazz.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.IRI.HQDM#MEMBER_OF} relationship type where a
     * {@link Class} may be a {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.IRI.HQDM#MEMBER_OF} one or more
     * {@link ClassOfClass}.
     *
     * @param classOfClass The ClassOfClass.
     * @return This builder.
     */
    public final ClassOfAbstractObjectBuilder member_Of(final ClassOfClass classOfClass) {
        classOfAbstractObject.addValue(MEMBER_OF, new IRI(classOfClass.getId()));
        return this;
    }

    /**
     * Returns an instance of ClassOfAbstractObject created from the properties set on this builder.
     *
     * @return The built ClassOfAbstractObject.
     * @throws HqdmException If the ClassOfAbstractObject is missing any mandatory properties.
     */
    public ClassOfAbstractObject build() throws HqdmException {
        if (classOfAbstractObject.hasValue(HAS_SUPERCLASS)
                && classOfAbstractObject.value(HAS_SUPERCLASS).isEmpty()) {
            throw new HqdmException("Property Not Set: has_superclass");
        }
        if (classOfAbstractObject.hasValue(MEMBER__OF)
                && classOfAbstractObject.value(MEMBER__OF).isEmpty()) {
            throw new HqdmException("Property Not Set: member__of");
        }
        if (classOfAbstractObject.hasValue(MEMBER_OF)
                && classOfAbstractObject.value(MEMBER_OF).isEmpty()) {
            throw new HqdmException("Property Not Set: member_of");
        }
        return classOfAbstractObject;
    }
}
