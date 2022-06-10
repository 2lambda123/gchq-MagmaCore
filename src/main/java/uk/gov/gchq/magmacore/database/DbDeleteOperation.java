package uk.gov.gchq.magmacore.database;

import java.util.function.Function;

import uk.gov.gchq.hqdm.iri.IRI;

/**
 * Class representing an invertible operation to delete a predicate.
 *
 * */
public class DbDeleteOperation implements Function<MagmaCoreDatabase, MagmaCoreDatabase> {
    private IRI subject;
    private IRI predicate;
    private String object;

    /**
     * Constructor.
     *
     * @param subject {@link IRI}
     * @param predicate {@link IRI}
     * @param object {@link String}
    */
    public DbDeleteOperation(final IRI subject, final IRI predicate, final String object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    /**
     * Apply the operation to a {@link MagmaCoreDatabase}.
     *
     * @param db {@link MagmaCoreDatabase}
     * */
    public MagmaCoreDatabase apply(final MagmaCoreDatabase db) {
        final var thing = db.get(subject);

        if (thing != null && thing.hasThisValue(predicate.getIri(), object)) {
            thing.removeValue(predicate.getIri(), object);
            db.update(thing);
            return db;
        }

        throw new RuntimeException(
                String.format("Triple not found for delete: %s, %s, %s", subject, predicate, object)
                );
    }

    /**
     * Invert a {@link DbDeleteOperation}.
     *
     * @param d the {@link DbDeleteOperation}
     * @return {@link DbCreateOperation}
    */
    public static DbCreateOperation invert(final DbDeleteOperation d) {
        return new DbCreateOperation(d.subject, d.predicate, d.object);
    }
}