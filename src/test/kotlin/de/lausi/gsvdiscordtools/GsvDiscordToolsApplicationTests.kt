package de.lausi.gsvdiscordtools

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.library.Architectures
import org.junit.jupiter.api.Test

class GsvDiscordToolsApplicationTests {

  @Test
  fun testArchitecture() {
    val classes = ClassFileImporter()
      .withImportOption(ImportOption.DoNotIncludeTests())
      .importPackages("de.lausi95.gsvrankedwatcherplayermanager")

    Architectures.onionArchitecture()
      .withOptionalLayers(true)
      .adapter("riot", "de.lausi95.gsvrankedwatcherplayermanager.adapter.riot")
      .adapter("mongo", "de.lausi95.gsvrankedwatcherplayermanager.adapter.mongo")
      .adapter("http", "de.lausi95.gsvrankedwatcherplayermanager.adapter.http")
      .domainModels("de.lausi95.gsvrankedwatcherplayermanager.domain.model.*")
      .applicationServices("de.lausi95.gsvrankedwatcherplayermanager.application")
      .check(classes)
  }
}
