package io.mgba.android

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import io.mgba.android.settings.AutoStateSettings
import io.mgba.android.settings.FastForwardModes

object HelpContent {
    private data class HelpRow(
        val nameRes: Int,
        val descriptionRes: Int,
        val descriptionArgs: Array<out Any> = emptyArray(),
    )

    private data class HelpSection(
        val titleRes: Int,
        val rows: List<HelpRow>,
    )

    fun createView(context: Context, autoStateIntervalSeconds: Int): View {
        val root = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(context, 20), dp(context, 8), dp(context, 20), dp(context, 16))
        }
        sections(autoStateIntervalSeconds).forEachIndexed { index, section ->
            addSection(context, root, section, isFirst = index == 0)
        }
        return ScrollView(context).apply {
            isFillViewport = false
            clipToPadding = false
            addView(root)
        }
    }

    private fun sections(autoStateIntervalSeconds: Int): List<HelpSection> {
        val interval = AutoStateSettings.coerceIntervalSeconds(autoStateIntervalSeconds)
        val fastValues = FastForwardModes.multiplierLabels.joinToString(", ")
        return listOf(
            HelpSection(
                R.string.help_section_toolbar,
                listOf(
                    row(R.string.help_row_pause_reset_name, R.string.help_row_pause_reset_desc),
                    row(R.string.help_row_fast_name, R.string.help_row_fast_desc),
                    row(R.string.help_row_rewind_name, R.string.help_row_rewind_desc),
                    row(R.string.help_row_toolbar_state_more_name, R.string.help_row_toolbar_state_more_desc),
                ),
            ),
            HelpSection(
                R.string.help_section_state_data,
                listOf(
                    row(R.string.help_row_load_save_name, R.string.help_row_load_save_desc),
                    row(R.string.help_row_auto_name, R.string.help_row_auto_desc),
                    row(R.string.help_row_data_name, R.string.help_row_data_desc),
                    row(R.string.help_row_cheat_patch_name, R.string.help_row_cheat_patch_desc),
                    row(R.string.help_row_exit_name, R.string.help_row_exit_desc),
                ),
            ),
            HelpSection(
                R.string.help_section_main,
                listOf(
                    row(R.string.help_row_main_actions_name, R.string.help_row_main_actions_desc),
                    row(R.string.help_row_library_name, R.string.help_row_library_desc),
                    row(R.string.help_row_bios_logs_name, R.string.help_row_bios_logs_desc),
                    row(R.string.help_row_settings_backup_name, R.string.help_row_settings_backup_desc),
                ),
            ),
            HelpSection(
                R.string.help_section_values,
                listOf(
                    row(
                        R.string.help_row_auto_values_name,
                        R.string.help_row_auto_values_desc,
                        interval,
                        AutoStateSettings.DefaultIntervalSeconds,
                        AutoStateSettings.MinIntervalSeconds,
                        AutoStateSettings.MaxIntervalSeconds,
                    ),
                    row(R.string.help_row_speed_values_name, R.string.help_row_speed_values_desc, fastValues),
                    row(R.string.help_row_video_values_name, R.string.help_row_video_values_desc),
                    row(R.string.help_row_audio_values_name, R.string.help_row_audio_values_desc),
                    row(R.string.help_row_input_values_name, R.string.help_row_input_values_desc),
                    row(R.string.help_row_sensor_tools_name, R.string.help_row_sensor_tools_desc),
                    row(R.string.help_row_language_values_name, R.string.help_row_language_values_desc),
                ),
            ),
        )
    }

    private fun row(nameRes: Int, descriptionRes: Int, vararg args: Any): HelpRow {
        return HelpRow(nameRes, descriptionRes, args)
    }

    private fun addSection(context: Context, root: LinearLayout, section: HelpSection, isFirst: Boolean) {
        root.addView(TextView(context).apply {
            text = context.getString(section.titleRes)
            textSize = 17f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(context.getColor(R.color.mgba_accent_active))
            setPadding(0, if (isFirst) 0 else dp(context, 18), 0, dp(context, 8))
        })
        section.rows.forEach { helpRow ->
            addRow(context, root, helpRow)
        }
    }

    private fun addRow(context: Context, root: LinearLayout, helpRow: HelpRow) {
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(context, 4), 0, dp(context, 12))
        }
        row.addView(TextView(context).apply {
            text = context.getString(helpRow.nameRes)
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(context.getColor(R.color.mgba_text_primary))
        })
        row.addView(TextView(context).apply {
            text = if (helpRow.descriptionArgs.isEmpty()) {
                context.getString(helpRow.descriptionRes)
            } else {
                context.getString(helpRow.descriptionRes, *helpRow.descriptionArgs)
            }
            textSize = 13f
            setLineSpacing(dp(context, 2).toFloat(), 1.0f)
            setTextColor(context.getColor(R.color.mgba_text_secondary))
        })
        root.addView(
            row,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            ),
        )
    }

    private fun dp(context: Context, value: Int): Int {
        return (value * context.resources.displayMetrics.density).toInt()
    }
}
