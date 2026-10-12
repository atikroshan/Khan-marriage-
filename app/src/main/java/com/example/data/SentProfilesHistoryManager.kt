package com.example.data

import android.content.Context
import android.content.SharedPreferences

object SentProfilesHistoryManager {
  private const val PREF_NAME = "sent_profiles_history_pref"
  private const val DEMO_INITIALIZED_KEY = "demo_initialized_flag_v1"

  /**
   * Cleans a phone number to standard 10 digits if possible, or stripped numeric format.
   */
  fun cleanPhoneNumber(phone: String): String {
    val digits = phone.filter { it.isDigit() }
    return if (digits.length >= 10) digits.takeLast(10) else digits
  }

  /**
   * Returns all profile codes sent to this phone number previously.
   */
  fun getSentProfileCodes(context: Context, phone: String): Set<String> {
    val clean = cleanPhoneNumber(phone)
    if (clean.isBlank()) return emptySet()
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Seed default demo history for test number 9876543210 so user can test immediate repeat vs new logic
    if (!prefs.contains(DEMO_INITIALIZED_KEY)) {
      prefs.edit()
        .putStringSet("9876543210", setOf("kmb001", "101"))
        .putBoolean(DEMO_INITIALIZED_KEY, true)
        .apply()
    }

    return prefs.getStringSet(clean, emptySet())?.toSet() ?: emptySet()
  }

  /**
   * Saves sent profile codes for the specified phone number.
   */
  fun saveSentProfileCodes(context: Context, phone: String, codes: Collection<String>) {
    val clean = cleanPhoneNumber(phone)
    if (clean.isBlank() || codes.isEmpty()) return
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val existing = prefs.getStringSet(clean, emptySet())?.toMutableSet() ?: mutableSetOf()
    existing.addAll(codes.map { it.trim() })
    prefs.edit().putStringSet(clean, existing).apply()
  }

  /**
   * Checks whether a candidate's profile code matches any code previously sent to this phone.
   */
  fun isProfileCodeRepeat(sentCodes: Set<String>, candidateCode: String): Boolean {
    if (sentCodes.isEmpty() || candidateCode.isBlank()) return false
    val trimmed = candidateCode.trim()
    val candidateDigits = trimmed.filter { it.isDigit() }

    return sentCodes.any { sent ->
      val sentTrimmed = sent.trim()
      val sentDigits = sentTrimmed.filter { it.isDigit() }

      sentTrimmed.equals(trimmed, ignoreCase = true) ||
        (candidateDigits.isNotBlank() && sentDigits.isNotBlank() && candidateDigits == sentDigits)
    }
  }

  /**
   * Clear history for a phone number.
   */
  fun clearHistory(context: Context, phone: String) {
    val clean = cleanPhoneNumber(phone)
    if (clean.isBlank()) return
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    prefs.edit().remove(clean).apply()
  }
}
