package com.example.vinicius.popularmoviesstage2;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*
 * Implementa o padrão singleton que encapsula a RequestQueue e outras funcionalidades
 * da biblioteca volley
 */
public class RequestQueueSingleton
{
   private static RequestQueueSingleton instance;
   private RequestQueue requestQueue;
   //private ImageLoader imageloader;
   private static Context context;

   /*
    * O conceito chave é que a RequestQueue deve ser instanciada com o contexto da aplicação,
    * não com o contexto da activity. Isto assegura que a RequestQueue vai durar por todo
    * tempo de vida do app em vez de ser recriado a cada vez que uma activity for recriada
    * (por exemplo quando o usuário gira o dispositivo)
    */
   private RequestQueueSingleton(Context context)
   {
      RequestQueueSingleton.context = context;
      requestQueue = getRequestQueue();

      /*
       * Imageloader é uma classe que lida com o carregamento e o cache de imagens de URLs.
       * Imageloader fornece um cache na memória que fica na frente do cache normal da
       * biblioteca volley, o que é importante para prevenir que a imagem pisque ao ser
       * recarregada.
       */
      //imageloader = new ImageLoader(requestQueue, new LruBitmapCache(context));
   }
   
   public static synchronized RequestQueueSingleton getInstance(Context context)
   {
      if(instance == null)
      {
         instance = new RequestQueueSingleton(context);
      }
      return instance;
   }
   
   public RequestQueue getRequestQueue()
   {
      if(requestQueue == null)
      {
         // getApplicationContext() is key, it keeps you from leaking the
         // Activity or BroadcastReceiver if someone passes one in.
         requestQueue = Volley.newRequestQueue(context.getApplicationContext());
      }
      return requestQueue;
   }
   
   public <T> void addToRequestQueue(Request<T> req)
   {
      /*
       * Assim que uma requisição é adicionada na fila de requisições a bibllioteca volley
       * executa uma thread que processa o cache e um conjunto de threads de requisições
       * de rede. Quando se adiciona um pedido na fila ele é pego pela thread de cache
       * e é analisado: se a requisição pode ser resolvida da cache, a resposta dessa
       * requisição que está no cache é transformada (transformada para a estrutura
       * de dados contida no objeto do tipo T) na thread de cache e a resposta
       * transformada é entregue na thread principal. Se a requisição não pode ser
       * resolvida na cache, ela é posta na fila de requisições pela rede. A primeira
       * thread de requisição de rede livre pega a requisição da fila, faz a chamada
       * HTTP, transforma a resposta recebida na chamada dentro da thread de requisição,
       * escreve a resposta na cache, e envia a resposta transformada de volta para a thread
       * principal
       */
      getRequestQueue().add(req);
   }
}
