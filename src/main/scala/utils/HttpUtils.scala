package utils

import scalaj.http.{Http, HttpResponse}

/**
  * Use Scalaj as the Http lib to fetch Get request
  */
object HttpUtils {

  def getContent(path: String): String = {
    val response: HttpResponse[String] = Http(path).asString
    if (response.code == 200) response.body else "error"
  }

  def concatHttpFullPath(path: String)(implicit host: String) = host + path

}
