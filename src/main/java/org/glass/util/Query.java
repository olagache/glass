/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.glass.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for paged queries in services.
 *
 * @author damien bourdette
 */
public class Query {
    public static final int DEFAULT_SIZE = 100;

    /**
     * 0 based page index
     */
    private int index;

    /**
     * Size of a page
     */
    private int size = DEFAULT_SIZE;

    private Query() {

    }

    public static Query index(int index) {
        if (index < 0) {
            index = 0;
        }

        Query query = new Query();

        query.index = index;

        return query;
    }

    public static Query oneBasedIndex(int index) {
        return index(index - 1);
    }

    public Query withSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("size must be stricly positive");
        }

        Query query = new Query();

        query.index = index;
        query.size = size;

        return query;
    }

    public <T> List<T> subList(List<T> list) {
        int start = Math.min(getStart(), list.size());
        int end = Math.min(getEnd(), list.size());

        return new ArrayList<T>(list.subList(start, end));
    }

    public String applySqlLimit(String sql) {
        StringBuilder builder = new StringBuilder();

        if (getStart() != 0) {
            builder.append("select * from ( select row_.*, rownum rownum_ from ( ");
        } else {
            builder.append("select * from ( ");
        }

        builder.append(sql);

        if (getStart() != 0) {
            builder.append(" ) row_ where rownum <= " + getEnd() + ") where rownum_ > " + + getStart());
        } else {
            builder.append(" ) where rownum <= " + getEnd());
        }

        return builder.toString();
    }

    public int getIndex() {
        return index;
    }

    public int getOneBasedIndex() {
        return index + 1;
    }

    public int getSize() {
        return size;
    }

    public int getStart() {
        return index * size;
    }

    public int getEnd() {
        return getStart() + size;
    }
}
