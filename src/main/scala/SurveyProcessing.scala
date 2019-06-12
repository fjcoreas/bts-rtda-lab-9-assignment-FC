import views.{AgeAvgByCountry, AgeGenderView, AvgProfessionalCodingExperienceView, DeveloperOpenSourcePercentageView, PercentageByEthnicityView, PercentageDevStudentsView, PercentageProgrammersLanguageView, PercentageProgrammersPlatformviews, PercentageSocialMediaView}
import org.apache.spark.sql.{DataFrame, Dataset, Encoders, SparkSession}
import org.apache.spark.sql.functions._

class SurveyProcessing(surveyDataFrame: DataFrame, spark: SparkSession) {

  def developerCount(): Long = {
    this.surveyDataFrame.count()
  }

  def createDeveloperOpenSourcePercentageView(): Dataset[DeveloperOpenSourcePercentageView] = {
    import spark.implicits._
    surveyDataFrame.groupBy("OpenSourcer")
      .count()
      .withColumn("percentage", (col("count") /
        sum("count").over()) * 100)
      .as[DeveloperOpenSourcePercentageView]
  }


  def createAgeGenderView(): Dataset[AgeGenderView] = {
    import spark.implicits._
    surveyDataFrame
      .withColumn("Gender", explode(split($"Gender", ";")))
      .groupBy("Gender")
      .agg(avg("Age1stCode").as("avg"))
      .orderBy(desc("avg")).as[AgeGenderView]
  }

  def createPercentageDevStudentsView(): Dataset[PercentageDevStudentsView] = {
    import spark.implicits._
    surveyDataFrame
      .groupBy("Student")
      .count()
      .withColumn("percentage", (col("count") / sum("count").over()) * 100)
      .orderBy(desc("percentage")).as[PercentageDevStudentsView]
  }

  def createAvgProfessionalCodingExperienceView(): Dataset[AvgProfessionalCodingExperienceView] = {
    import spark.implicits._
    surveyDataFrame
      .withColumn("DevType", explode(split($"DevType", ";")))
      .groupBy("DevType")
      .agg(avg("YearsCodePro").as("avg"))
      .orderBy(desc("avg")).as[AvgProfessionalCodingExperienceView]
  }

  def createPercentageByEthnicityView(): Dataset[PercentageByEthnicityView] = {
    import spark.implicits._
    surveyDataFrame
      .withColumn("Ethnicity", explode(split($"Ethnicity", ";")))
      .groupBy("Ethnicity")
      .count()
      .withColumn("percentage", (col("count") / sum("count").over()) * 100)
      .orderBy(desc("percentage")).as[PercentageByEthnicityView]
  }

  def createPercentageSocialMediaView(): Dataset[PercentageSocialMediaView] = {
    import spark.implicits._
    surveyDataFrame
      .groupBy("SocialMedia").count()
      .withColumn("percentage", (col("count") / sum("count").over()) * 100)
      .orderBy(desc("percentage")).as[PercentageSocialMediaView]
  }

  def createAgeAvgByCountry(): Dataset[AgeAvgByCountry] = {
    import spark.implicits._
    surveyDataFrame
      .groupBy("Country")
      .agg(avg("Age").as("avg"))
      .orderBy(desc("avg")).as[AgeAvgByCountry]
  }
  def createPercentageProgrammersLanguage(): Dataset[PercentageProgrammersLanguageView] = {
    import spark.implicits._
    surveyDataFrame
      .withColumn("LanguageWorkedWith", explode(split($"LanguageWorkedWith",";")))
      .groupBy("LanguageWorkedWith").count()
      .withColumn("percentage", (col("count")/sum("count").over())*100)
      .orderBy(desc("percentage")).as[PercentageProgrammersLanguageView]
  }

  def createPercentageProgrammersPlatform(): Dataset[PercentageProgrammersPlatformviews] = {
    import spark.implicits._
    surveyDataFrame
      .withColumn("PlatformWorkedWith", explode(split($"PlatformWorkedWith",";")))
      .groupBy("PlatformWorkedWith").count()
      .withColumn("percentage", (col("count")/sum("count").over())*100)
      .orderBy(desc("percentage")).as[PercentageProgrammersPlatformviews]
  }

}

