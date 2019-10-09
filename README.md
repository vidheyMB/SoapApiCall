# SoapApiCall
MVVM SoapApiCall with rxjava2.


How to - 
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.vidheyMB:SoapApiCall:Tag'
	}
  
  
  For Realse Check:
  https://jitpack.io/v/vidheyMB/SoapApiCall.svg
  https://jitpack.io/#vidheyMB/SoapApiCall


How To Use Library.

           VMac()
                .url(url)
                .methodName(methodName)
                .methodBase(methodBase)
                .request(request)
                .parentTags("a:Catalogue")
                .childTags(
                    "a:TotalCash",
                    "a:CatalogueId",
                    "a:PointsRequired",
                    "a:TermsCondition",
                    "a:VendorName",
                    "a:VendorId",
                    "a:RedemptionDate"
                )
                .execute<ProductClass> {

                    Log.d("Result", "List_Data : $it")
                }
                
 The ChildTags and ProductClass should match, ProductClass.kt is a data class, 
 remove the prefix of Tags in data class, all the parameters should be "String".                
                
