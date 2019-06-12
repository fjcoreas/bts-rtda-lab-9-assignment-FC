import org.apache.spark.sql.{Dataset, SparkSession}
import views.{AgeAvgByCountry, AgeGenderView, DeveloperOpenSourcePercentageView, PercentageByEthnicityView, PercentageDevStudentsView, PercentageProgrammersLanguageView, PercentageProgrammersPlatformviews, PercentageSocialMediaView}

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .config("spark.es.nodes", "elasticsearch")
      .config("spark.es.port", "9200")
      .config("spark.es.index.auto.create", "true")
      .getOrCreate()

    val surveyInputPath: String = args(0);

    val surveyDataFrame = spark.read.option("header", "true").csv(surveyInputPath)
    val surveyProcessing: SurveyProcessing = new SurveyProcessing(surveyDataFrame, spark);

    val developerOpenSourcePercentageView : Dataset[DeveloperOpenSourcePercentageView] =
      surveyProcessing.createDeveloperOpenSourcePercentageView()

    val ageGenderView: Dataset[AgeGenderView] = surveyProcessing.createAgeGenderView()

    val percentageDevStudentsView: Dataset[PercentageDevStudentsView] = surveyProcessing createPercentageDevStudentsView()

    val percentageByEthnicityView: Dataset[PercentageByEthnicityView] = surveyProcessing.createPercentageByEthnicityView()

    val percentageSocialMediaView: Dataset[PercentageSocialMediaView]  = surveyProcessing.createPercentageSocialMediaView()

    val AgeAvgByCountry: Dataset[AgeAvgByCountry]  = surveyProcessing.createAgeAvgByCountry()

    val PercentageProgrammersLanguageView: Dataset[PercentageProgrammersLanguageView] =surveyProcessing.createPercentageProgrammersLanguage()

    val PercentageProgrammersPlatformView: Dataset[PercentageProgrammersPlatformviews] =surveyProcessing.createPercentageProgrammersPlatform()

    ElasticViewWriter.writeView(developerOpenSourcePercentageView, "DeveloperOpenSourcePercentageView")
    ElasticViewWriter.writeView(ageGenderView, "developerOpenSourcePercentageView")
    ElasticViewWriter.writeView(percentageDevStudentsView, "percentageDevStudentsView")
    ElasticViewWriter.writeView(percentageByEthnicityView, "percentageByEthnicityView")
    ElasticViewWriter.writeView(percentageSocialMediaView, "percentageSocialMediaView")
    ElasticViewWriter.writeView(AgeAvgByCountry, "AgeAvgByCountry")
    ElasticViewWriter.writeView(PercentageProgrammersLanguageView, "PercentageProgrammersLanguageView")
    ElasticViewWriter.writeView(PercentageProgrammersPlatformView, "PercentageProgrammersPlatformView")
    spark.stop();
  }


}
