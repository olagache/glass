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

package com.github.dbourdette.glass.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
public class QueryTest {
    private List<Long> longs = new ArrayList<Long>();

    @Before
    public void fill() {
       for (int i = 1; i <= 100; i++) {
           longs.add(Long.valueOf(i));
       }
    }

    @Test
    public void subListFirstPage() {
        Query query = Query.oneBasedIndex(1).withSize(10);

        longs = query.subList(longs);

        Assert.assertEquals("Sublist size should be 10", 10, longs.size());
        Assert.assertEquals("Fith element should be 5", Long.valueOf(5), longs.get(4));
    }

    @Test
    public void subList() {
        Query query = Query.oneBasedIndex(2).withSize(10);

        longs = query.subList(longs);

        Assert.assertEquals("Sublist size should be 10", 10, longs.size());
        Assert.assertEquals("Fith element should be 15", Long.valueOf(15), longs.get(4));
    }

    @Test
    public void subListWithSmallInitialList() {
        Query query = Query.oneBasedIndex(1).withSize(200);

        longs = query.subList(longs);

        Assert.assertEquals("Sublist size should be 100", 100, longs.size());
        Assert.assertEquals("Fith element should be 5", Long.valueOf(5), longs.get(4));
    }
}
