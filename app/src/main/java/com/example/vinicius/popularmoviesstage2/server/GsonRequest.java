package com.example.vinicius.popularmoviesstage2.server;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/*
 * Classe que implementa uma requisisção REST customizada.
 *
 * Para implementar uma requisição customizada o que precisa ser feito é:
 *
 * 	- Extender a classe Request<T> onde <T> representa o tipo de resposta transformada que a
 * 	requisição espera. Se a resposta transformada é uma string, por exemplo, crie a requisição
 * 	customizada extendendo Request<String>.
 *
 * 	- Implementar os métodos abstratos parseNetworkResponse() e deliverResponse().
 *
 * Gson é uma biblioteca para a conversão de objetos java para JSON e de JSON para objetos java
 * utilizando reflection (ver o link: http://www.devmedia.com.br/conhecendo-java-reflection/29148)
 * Você pode definir objetos Java que possuam atributos com os mesmos nomes que as suas chaves JSON
 * correspondentes, passar esse objeto juntamente com uma String json para o método fromJson e ele
 * vai retornar um objeto com estes atributos preenchidos.
 */
public class GsonRequest<T> extends Request<T>
{
	private final Gson gson = new Gson();
	private final Class<T> clazz;
	private final Type type;
	private final Map<String, String> headers;
	private final Map<String, String> params;
	private final Response.Listener<T> listener;

	/**
	 * Make a GET request and return a parsed object from JSON.
	 *
	 * @param type    request type: GET, POST, DELETE...
	 * @param url     URL of the request to make
	 * @param clazz   Relevant class object, for Gson's reflection
	 * @param headers Map of request headers
	 */
	public GsonRequest(int requestType, String url, Class<T> clazz, Type type, Map<String, String> headers,
							 Map<String, String> params, Response.Listener<T> listener,
							 Response.ErrorListener errorListener)
	{
		super(requestType, url, errorListener);
		this.clazz = clazz;
		this.type = type;
		this.headers = headers;
		this.params = params;
		this.listener = listener;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError
	{
		return headers != null ? headers : super.getHeaders();
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError
	{
		return params != null ? params : super.getParams();
	}

	/*
	 * Volley chama de volta na main thread com o objeto retornado no método parseNetworkResponse
	 */
	@Override
	protected void deliverResponse(T response)
	{
		listener.onResponse(response);
	}

	/*
	 * Este método tem como parametro NetworkResponse que contém o payload da resposta
	 * como um byte[], HTTP status code, e cabeçalhos de resposta.
	 * A implementação deve retornar um Response<T>, que contém o objeto de resposta e
	 * metadados de cache ou um erro, no caso de falha da transformação.
	 * Se o protocolo usado na requisição tem semantica não padrão de cache, é possível
	 * construir o seu próprio Cache.Entry, mas a maioria das requisições são satisfeitas
	 * como na implementação abaixo
	 */
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response)
	{
		try
		{
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

			if(type == null)
			{
				return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
			}
			else
			{
				return (Response<T>) Response.success(gson.fromJson(json, type), HttpHeaderParser.parseCacheHeaders(response));
			}
		}
		catch(UnsupportedEncodingException e)
		{
			return Response.error(new ParseError(e));
		}
		catch(JsonSyntaxException e)
		{
			return Response.error(new ParseError(e));
		}
	}
}