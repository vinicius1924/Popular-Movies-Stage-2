package com.example.vinicius.popularmoviesstage2.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.example.vinicius.popularmoviesstage2.RequestQueueSingleton;

import java.lang.reflect.Field;
import java.util.Set;

public final class VolleyUtils
{
	private static final String VOLLEYUTILSTAG = "VolleyUtils";

	public static int getNumberOfRequestsInQueue(Context context) throws NoSuchFieldException
	{
		RequestQueue mVolleyRequestQueue = RequestQueueSingleton.getInstance(context.getApplicationContext()).getRequestQueue();
		Field mCurrentRequests = null;

		try
		{
			mCurrentRequests = RequestQueue.class.getDeclaredField("mCurrentRequests");
			mCurrentRequests.setAccessible(true);

			Set<Request> requestsQueue = (Set<Request>) mCurrentRequests.get(mVolleyRequestQueue);

			synchronized(requestsQueue)
			{
				if(requestsQueue != null)
				{
					return requestsQueue.size();
				}
				else
				{
					throw new NoSuchFieldException("mCurrentRequests field not found in RequestQueue");
				}
			}
		}
		catch(NoSuchFieldException e)
		{
			Log.e(VOLLEYUTILSTAG, e.toString());
		}
		catch(IllegalAccessException e)
		{
			Log.e(VOLLEYUTILSTAG, e.toString());
		}
		finally
		{
			mCurrentRequests.setAccessible(false);
		}

		return -1;
	}

	public static boolean isPendingToRequest(Context context, String tag)
	{
		RequestQueue mVolleyRequestQueue = RequestQueueSingleton.getInstance(context.getApplicationContext()).getRequestQueue();
		Field mCurrentRequests = null;

		try
		{
			mCurrentRequests = RequestQueue.class.getDeclaredField("mCurrentRequests");

			mCurrentRequests.setAccessible(true);

			Set<Request> requestsQueue = (Set<Request>) mCurrentRequests.get(mVolleyRequestQueue);

			for (final Request<?> request : requestsQueue )
			{
				if(request.getTag().equals(tag))
				{
					return true;
				}
			}
		}
		catch(NoSuchFieldException e)
		{
			Log.e(VOLLEYUTILSTAG, e.toString());
		}
		catch(IllegalAccessException e)
		{
			Log.e(VOLLEYUTILSTAG, e.toString());
		}
		finally
		{
			mCurrentRequests.setAccessible(false);
		}

		return false;
	}
}
