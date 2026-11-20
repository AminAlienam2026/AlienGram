package site.aliengram.app

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {
    private var stars = 0
    private var premium = false
    private val names = listOf("Alien", "Datinini", "Parham", "Amirali", "AlienGram Group")
    private val previews = listOf("Welcome to AlienGram 👽", "یادت نره پیاممو ببینی", "آنلاین بازی؟", "سلام دادا", "گروه تازه ساخته شد")
    private val avatars = listOf("👽", "🦊", "🎮", "🧑", "👥")
    private lateinit var chatList: LinearLayout
    private lateinit var wallet: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chatList = findViewById(R.id.chatList)
        wallet = findViewById(R.id.wallet)
        refreshWallet()
        buildChats(names.indices.toList())
        findViewById<View>(R.id.admin).setOnClickListener { showAdminMenu() }
        wallet.setOnClickListener { showWallet() }
        findViewById<EditText>(R.id.search).addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { buildChats(names.indices.filter { names[it].contains(s.toString(), true) }) }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
        findViewById<View>(R.id.navContacts).setOnClickListener { showSimple("Contacts", "Contact discovery will be added in the next build.") }
        findViewById<View>(R.id.navSettings).setOnClickListener { showSettings() }
    }

    private fun refreshWallet() { wallet.text = "⭐ $stars" }
    private fun buildChats(indices: List<Int>) {
        chatList.removeAllViews()
        indices.forEach { i ->
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL; setPadding(14, 12, 14, 12) }
            val avatar = TextView(this).apply { text = avatars[i]; textSize = 28f; gravity = Gravity.CENTER; setBackgroundColor(Color.rgb(36,51,67)); layoutParams = LinearLayout.LayoutParams(58,58) }
            val col = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(12,0,0,0); layoutParams = LinearLayout.LayoutParams(0, -2, 1f) }
            val name = TextView(this).apply { text = names[i] + if (i==0 && premium) " 💎" else ""; textSize=16f; setTextColor(Color.WHITE) }
            val preview = TextView(this).apply { text = previews[i]; textSize=13f; setTextColor(Color.rgb(151,166,181)); maxLines=1 }
            col.addView(name); col.addView(preview)
            val time = TextView(this).apply { text = "12:4${i}"; textSize=11f; setTextColor(Color.rgb(131,149,165)) }
            row.addView(avatar); row.addView(col); row.addView(time)
            row.setOnClickListener { openChat(i) }
            chatList.addView(row)
            val line = View(this).apply { setBackgroundColor(Color.rgb(31,44,56)); layoutParams=LinearLayout.LayoutParams(-1,1) }
            chatList.addView(line)
        }
    }
    private fun openChat(i: Int) {
        val layout = LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setBackgroundColor(Color.rgb(16,24,32)); setPadding(16,16,16,16) }
        val heading = TextView(this).apply { text="←   ${names[i]}"; textSize=20f; setTextColor(Color.WHITE); setPadding(0,0,0,12) }
        val history = TextView(this).apply { text="${previews[i]}\n\nMessages are local demo only. Backend sync comes later."; textSize=16f; setTextColor(Color.rgb(218,226,235)); layoutParams=LinearLayout.LayoutParams(-1,0,1f) }
        val inputRow = LinearLayout(this).apply { orientation=LinearLayout.HORIZONTAL }
        val input = EditText(this).apply { hint="Message"; setTextColor(Color.WHITE); setHintTextColor(Color.GRAY); setBackgroundColor(Color.rgb(31,44,56)); layoutParams=LinearLayout.LayoutParams(0, -2,1f) }
        val send = Button(this).apply { text="Send" }
        inputRow.addView(input); inputRow.addView(send)
        layout.addView(heading); layout.addView(history); layout.addView(inputRow)
        val dlg = AlertDialog.Builder(this).setView(layout).create()
        heading.setOnClickListener { dlg.dismiss() }
        send.setOnClickListener { val msg=input.text.toString().trim(); if(msg.isNotEmpty()){ history.text="${history.text}\n\nYou: $msg"; input.text.clear() } }
        dlg.show()
    }
    private fun showAdminMenu() {
        AlertDialog.Builder(this).setTitle("AlienGram Admin (demo)").setItems(arrayOf("Add Stars", "Remove Stars", "Grant Premium", "Revoke Premium")) { _, which -> when(which){0->changeStars(true);1->changeStars(false);2->{premium=true; refreshWallet(); showSimple("Premium", "Premium enabled for this demo account.")};3->{premium=false; refreshWallet(); showSimple("Premium", "Premium revoked.")} } }.show()
    }
    private fun changeStars(add: Boolean) {
        val input=EditText(this).apply { hint="Amount"; inputType=2 }
        AlertDialog.Builder(this).setTitle(if(add) "Add Stars" else "Remove Stars").setView(input).setPositiveButton("Apply") { _,_-> val n=input.text.toString().toIntOrNull()?:0; stars=(if(add) stars+n else (stars-n).coerceAtLeast(0)); refreshWallet() }.setNegativeButton("Cancel",null).show()
    }
    private fun showWallet() = showSimple("Alien Stars", "Your demo balance: ⭐ $stars\n\nStars ledger and admin tools will be connected to the server later.")
    private fun showSettings() = showSimple("Settings", "AlienGram\nVersion 0.1 demo\nPremium: ${if(premium) "Active" else "Inactive"}\nStars: $stars\n\nAccount, privacy, theme and notification settings will be added next.")
    private fun showSimple(title: String, message: String) { AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK",null).show() }
}
