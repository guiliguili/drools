/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.regioncache;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This class implements the <tt>Set</tt> interface, backed by a ConcurrentHashMap instance.
 *
 * @param <E>
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements java.io.Serializable
{
	private static final long serialVersionUID = 2L;
	private transient Set<E> target;

	public ConcurrentHashSet()
	{
		this(Collections.EMPTY_LIST);
	}

	public ConcurrentHashSet(final Collection<? extends E> collection)
	{
		this(Math.max((int) (collection.size() / .75f) + 1, 16));
		addAll(collection);
	}

	public ConcurrentHashSet(final int initialCapacity, final float loadFactor)
	{
		this(new ConcurrentHashMap<E, Boolean>(initialCapacity, loadFactor, 16));
	}

	public ConcurrentHashSet(final int initialCapacity)
	{
		this(new ConcurrentHashMap<E, Boolean>(initialCapacity));
	}

	private ConcurrentHashSet(final ConcurrentHashMap<E, Boolean> map)
	{
		target = Collections.newSetFromMap(map);
	}

	@Override
	public Iterator<E> iterator()
	{
		return target.iterator();
	}

	@Override
	public int size()
	{
		return target.size();
	}

	@Override
	public boolean isEmpty()
	{
		return target.isEmpty();
	}

	@Override
	public boolean contains(final Object element)
	{
		return target.contains(element);
	}

	@Override
	public boolean add(final E element)
	{
		return target.add(element);
	}

	@Override
	public boolean remove(final Object element)
	{
		return target.remove(element);
	}

	@Override
	public void clear()
	{
		target.clear();
	}


	private void writeObject(final java.io.ObjectOutputStream stream) throws java.io.IOException
	{
		stream.defaultWriteObject();
		stream.writeInt(target.size());

		for (final Iterator<E> i = target.iterator(); i.hasNext(); )
		{
			stream.writeObject(i.next());
		}
	}

	private void readObject(final ObjectInputStream inputStream) throws ClassNotFoundException, IOException
	{
		inputStream.defaultReadObject();

		target = Collections.newSetFromMap(new ConcurrentHashMap<E, Boolean>());

		final int size = inputStream.readInt();
		for (int i = 0; i < size; i++)
		{
			final E object = (E) inputStream.readObject();
			target.add(object);
		}
	}
}
