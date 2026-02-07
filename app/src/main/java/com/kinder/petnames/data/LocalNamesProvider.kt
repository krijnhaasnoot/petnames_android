package com.kinder.petnames.data

import android.content.Context
import com.kinder.petnames.domain.BundledNamesData
import com.kinder.petnames.domain.LocalNameEntry
import com.kinder.petnames.domain.NameSet
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalNamesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var bundledData: BundledNamesData? = null
    private var allNames: MutableList<LocalNameEntry> = mutableListOf()
    private val nameSetMap: MutableMap<String, NameSet> = mutableMapOf()
    
    private val json = Json { ignoreUnknownKeys = true }
    
    init {
        loadBundledData()
        buildNameIndex()
    }
    
    /**
     * Load bundled JSON from assets
     */
    private fun loadBundledData() {
        try {
            val inputStream = context.assets.open("bundled_names.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            bundledData = json.decodeFromString<BundledNamesData>(jsonString)
            println("✅ Loaded bundled names: ${bundledData?.nameSets?.size ?: 0} sets")
        } catch (e: Exception) {
            println("❌ Failed to load bundled names: ${e.message}")
        }
    }
    
    /**
     * Build index of all names for quick lookup (with deduplication)
     */
    private fun buildNameIndex() {
        allNames.clear()
        nameSetMap.clear()
        
        val data = bundledData ?: return
        val seenNames = mutableSetOf<String>()
        
        for (nameSet in data.nameSets) {
            // Create NameSet for mapping
            nameSetMap[nameSet.slug] = NameSet(
                id = nameSet.slug, // Use slug as ID for local sets
                slug = nameSet.slug,
                title = nameSet.title,
                language = nameSet.language,
                style = nameSet.style,
                description = nameSet.description
            )
            
            for (bundledName in nameSet.names) {
                val nameLower = bundledName.name.lowercase()
                
                // Skip duplicates
                if (seenNames.contains(nameLower)) continue
                seenNames.add(nameLower)
                
                allNames.add(
                    LocalNameEntry(
                        name = bundledName.name,
                        gender = bundledName.gender,
                        setSlug = nameSet.slug,
                        setTitle = nameSet.title,
                        language = nameSet.language,
                        style = nameSet.style
                    )
                )
            }
        }
        
        // Shuffle for variety
        allNames.shuffle()
        
        println("📊 Indexed ${allNames.size} unique names from ${nameSetMap.size} sets")
    }
    
    /**
     * Get filtered names
     */
    fun getNames(
        enabledSetIds: List<String>,
        gender: String,
        startsWith: String,
        maxLength: Int,
        excludeNames: Set<String>,
        limit: Int
    ): List<LocalNameEntry> {
        println("🔍 Filtering names with enabledSetIds: $enabledSetIds")
        
        return allNames
            .filter { entry ->
                // Filter by enabled sets - EXACT slug matching only
                val setMatches = enabledSetIds.isEmpty() || enabledSetIds.any { setId ->
                    entry.setSlug.equals(setId, ignoreCase = true)
                }
                
                // Filter by gender
                val genderMatches = gender == "any" || entry.gender.equals(gender, ignoreCase = true)
                
                // Filter by starts with
                val startsWithMatches = startsWith == "any" || 
                    entry.name.lowercase().startsWith(startsWith.lowercase())
                
                // Filter by max length
                val lengthMatches = maxLength == 0 || entry.name.length <= maxLength
                
                // Exclude already swiped names
                val notExcluded = !excludeNames.contains(entry.name.lowercase())
                
                setMatches && genderMatches && startsWithMatches && lengthMatches && notExcluded
            }
            .also { 
                println("✅ Found ${it.size} matching names") 
            }
            .take(limit)
    }
    
    /**
     * Get name sets for UI
     */
    fun getNameSets(): List<NameSet> {
        return nameSetMap.values.toList()
    }
    
    /**
     * Get name set by slug
     */
    fun getNameSet(slug: String): NameSet? {
        return nameSetMap[slug]
    }
}
