package com.vidhey.vmac

/**
 *  Create By - Vidhey on 04/09/19.
 *
 *  if any modification made mention here.
 *
 */


import android.util.Log
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList

open class VMac {

    protected lateinit var MethodName: String
    protected lateinit var URL: String
    protected lateinit var MethodBaseName: String
    protected lateinit var Request: String

    public val ParentTagsNormal = ArrayList<String>()
    public val ChildTags = ArrayList<String>()
    public val ChildTagsNormal = ArrayList<String>()

    var ErrorCatch: String? = null

    fun methodName(Method_Name: String): VMac {
        this.MethodName = Method_Name
        return this
    }

    fun url(URL: String): VMac {
        this.URL = URL
        return this
    }

    fun request(Request: String): VMac {
        this.Request = Request
        return this
    }

    fun methodBase(MethodBaseName: String): VMac {
        this.MethodBaseName = MethodBaseName
        return this
    }

    fun parentTags(vararg Tags: String): VMac {
        for (item in Tags) {
            ParentTagsNormal.add(item)
        }
        return this
    }

    fun childTags(vararg Tags: String): VMac {
        for (item in Tags) {
            ChildTags.add(splitString(item))
            ChildTagsNormal.add(item)
        }
        return this
    }


    inline fun <reified T> execute(crossinline checkValue: (va: ArrayList<T>?) -> Unit): Disposable {

        return ApiCall(Request, MethodBaseName + MethodName, URL)
            .call()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                val document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                val result =
                    document.parse(ByteArrayInputStream(it.toString().toByteArray()))

                when {
                    ParentTagsNormal.isNotEmpty() -> return@map listValue<T>(result)

                    ChildTagsNormal.isNotEmpty() -> return@map singleValue<T>(result)
                    else -> return@map "No Tags have given !!"
                }
            }
            .subscribe({

                //                        BaseViewModel().printLog("Subscribe_Response : $it")

                when (it) {
                    null -> Log.e("Error","Data is Null : $ErrorCatch")
                    is String -> Log.d("Warning","Subscribe_Response : $it")
                    else -> checkValue(it as ArrayList<T>)
                }

            },
                {
                    Log.e("VMac_Error","VMac_Error : $it")
                    checkValue(null)
                })

    }


    fun executes(checkValue: (va: Any?) -> Unit): Disposable {

        return ApiCall(Request, MethodBaseName + MethodName, URL)
            .call()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                val document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                return@map document.parse(ByteArrayInputStream(it.toString().toByteArray()))
            }
            .subscribe({
                Log.d("Warning","Subscribe_Response : $it")
                checkValue(it)

            },
                {
                    Log.e("Error","Subscribe_Error : $it")
                    checkValue(null)
                })
    }


    protected inline fun <reified T> listValue(result: Document): ArrayList<T>? {

        var nodeList: NodeList? = null

        val arrayList = ArrayList<T>()

        val gson = Gson()

        for (item in ParentTagsNormal) {

            try {
                nodeList = result.getElementsByTagName(item)
            } catch (e: Exception) {
                ErrorCatch = " (ListValue Method) : XML ParentTag Not Found !! OR $e "
                return null
            }

            if (!nodeList.item(0).hasChildNodes()) {
                ErrorCatch = " (ListValue Method) : XML List of Data Not Found !! "
                return null
            }

            val map = WeakHashMap<String, String>()

            for (i in 0 until nodeList.length) {


                for (item in ChildTagsNormal.indices) {
                    map[ChildTags[item]] = getElementByTagNameListValue(nodeList, i, item)
                }

                arrayList.add(gson.fromJson(gson.toJson(map),T::class.java))
                map.clear()
            }


        }

        System.gc()

        return arrayList
    }


    protected inline fun <reified T> singleValue(result: Document): ArrayList<T> {

        val arrayList = ArrayList<T>()

        val gson = Gson()

        val map = WeakHashMap<String, String>()

        for (item in ChildTagsNormal.indices) {
            map[ChildTags[item]] = getElementsByTagName(result, ChildTagsNormal[item])
        }

        arrayList.add(gson.fromJson(gson.toJson(map),T::class.java))
        map.clear()

        System.gc()

        return arrayList
    }


    protected fun getElementsByTagName(result: Document, item: String): String {
        return try {
            result.getElementsByTagName(item).item(0).textContent.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "-"
        }
    }

    protected fun getElementByTagNameListValue(nodeList: NodeList, i: Int, item: Int): String {
        return try {
            (nodeList.item(i) as Element).getElementsByTagName(ChildTagsNormal[item]).item(0)
                .textContent.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "-"
        }
    }


    private fun splitString(string: String): String {
        return if (string.contains(":"))
            string.split(":")[1]
        else
            string
    }
}