/**
 * Copyright 2014 PubNative GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.pubnative.library.task;

import java.util.ArrayList;

import net.pubnative.library.PubNativeContract;
import net.pubnative.library.model.request.AdRequest;
import net.pubnative.library.model.response.Ad;
import net.pubnative.library.model.response.NativeAd;

import org.droidparts.concurrent.task.AsyncTaskResultListener;
import org.droidparts.concurrent.task.SimpleAsyncTask;
import org.droidparts.persist.serializer.JSONSerializer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

public class GetAdsTask<T extends Ad> extends SimpleAsyncTask<ArrayList<T>>
{
    private final AdRequest                    adRequest;
    private final JSONSerializer<? extends Ad> serializer;

    public GetAdsTask(Context ctx, AdRequest adRequest, AsyncTaskResultListener<ArrayList<T>> resultListener)
    {
        super(ctx, resultListener);
        this.adRequest = adRequest;
        Class<? extends Ad> cls;
        switch (adRequest.getAdFormat())
        {
        default:
            cls = NativeAd.class;
        break;
        }
        serializer = new JSONSerializer<T>((Class)cls, ctx);
    }

    @Override
    protected ArrayList<T> onExecute() throws Exception
    {
        JSONObject obj = new GetAdsJSONTask(getContext(), adRequest, null).onExecute();
        JSONArray arr = obj.getJSONArray(PubNativeContract.Response.ADS);
        ArrayList<T> list = (ArrayList<T>) serializer.deserializeAll(arr);
        return list;
    }
}
