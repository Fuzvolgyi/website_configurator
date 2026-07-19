# Casa Gabriel — Admin User Guide

This guide explains how to use the admin panel to manage your website content.

---

## Accessing the Admin Panel

1. Open your browser and navigate to `/admin` (e.g., `https://yourdomain.com/admin`).
2. Enter your credentials:
   - **Username:** admin
   - **Password:** (provided by the system administrator)
3. Click **Sign In**.

---

## Changing the Language

The admin panel supports three languages: **Español**, **English**, and **Magyar**.

1. In the top-right toolbar, click the language dropdown.
2. Select your preferred language from the list.
3. The interface updates immediately.

---

## Preview & Edit (Inline CMS)

This is the primary way to manage your website content. You see the real page and can click directly on any text or image to edit it.

### How to access

1. Click **Preview & Edit** in the left sidebar.
2. The Home page renders with a dark toolbar at the top.
3. Use the **page tabs** (Home, Services, Apartments, Contact) to switch between pages.

### Editing text

1. Hover over any text — editable elements show a **dashed teal outline** with a ✏️ pencil icon.
2. Click the text to open the editor dialog.
3. Modify the content in your current admin language.
4. Click **Save & Translate**.

**What happens when you save:**
- The system attempts to auto-translate to all configured languages using DeepL AI.
- **Nothing is saved until all translations are ready** — either AI succeeds or you enter them manually.

**If translation fails:**
1. A yellow warning shows which languages failed.
2. Click **Retry** — the system tries again.
3. If retry fails, click **Enter Manually** — textareas appear for all languages (including your source language, pre-filled).
4. Fill in all translations and click **Save All**.

### Editing images

1. Hover over any image area — shows a **teal border** with a 📷 camera icon at the bottom-right.
2. Click the image area (not on text overlays) to open the file picker.
3. Select an image (JPEG, PNG, or WebP, max 5 MB).
4. The image uploads and previews immediately.
5. A **Confirm / Revert** bar appears at the bottom of the image:
   - **✓ Confirm** — saves permanently, deletes the old image from storage.
   - **✕ Revert** — discards the new upload, restores the previous image.

**Important:** Nothing is permanently saved until you click Confirm.

### What's editable on each page

**Home:**
- Hero title, subtitle, button text, badge
- Value section title, card titles and descriptions
- Room card titles, descriptions, prices, badges
- All background images (hero, value card, room cards, CTA)

**Services:**
- Hero title and subtitle
- Policies text (check-out time, minimum stay)
- All bento card titles and descriptions
- Atmosphere section title, text, and accordion content
- Card images and atmosphere image

**Apartments:**
- Hero title and subtitle
- All card titles, descriptions, amenities (each individually), prices, badges
- "View details" link text, CTA section
- All card images

**Contact:**
- Hero title and subtitle
- Contact info labels (phone, email, address)
- Location section title and text
- Form labels and button text
- Google Maps embed URL (click the map in preview to change location)
- Map address overlay text

### Changing the Google Maps location

1. Go to [Google Maps](https://maps.google.com) and find your location.
2. Click **Share** → **Embed a map** → copy the URL from the `src="..."` attribute.
3. In Preview mode, navigate to the **Contact** page tab.
4. Click on the **map area** — the text editor opens with the embed URL.
5. Paste the new URL (starts with `https://www.google.com/maps/embed?pb=...`).
6. Click **Save & Translate** — the map updates immediately.

### Exiting preview

Click the **Exit Preview** button in the top-right of the dark toolbar.

---

## Sites Management

**What are sites?**
Sites represent the main pages of your website (Home, Services, Apartments, Contact). You can toggle whether each site is active or inactive.

**How to activate/deactivate:**

1. Click **Sites** in the left sidebar.
2. Click the **radio button** in the Status column to toggle.
3. The change saves instantly — a confirmation message appears below the table.

**What happens when a site is inactive?**
Inactive sites will not be visible to visitors on the public website.

---

## Pictures Management

For managing gallery-style images associated with a site.

**How to add:** Select a site → Click "Add Picture" → Upload file → Fill alt text and display order → Save.

**How to edit:** Click the picture row → Modify fields → Save.

**How to delete:** Click the trash icon → Confirm deletion.

---

## Texts Management (Table View)

For bulk management of translation keys. Useful for managing content not visible in the preview, or for detailed key management.

**How to add:** Select a site → Click "Add Text" → Enter translation key and content → Create.

**How to edit:** Click the text row → Modify → Save.

**How to delete:** Click the trash icon → Confirm.

---

## Multi-Language System

All text content is stored per-language in the database.

**How it works:**
- When you edit via Preview & Edit, the system auto-translates using DeepL AI.
- The admin panel language determines the source language for translation.
- If you're working in English and save, it auto-translates to Spanish and Hungarian.
- If auto-translation fails, you can enter translations manually.
- Nothing is saved partially — all languages must be ready before persisting.

**To manually fix a translation:** Switch the admin language and edit that text again via Preview.

**Supported languages (configurable via environment variable):**
- English (EN)
- Spanish (ES)
- Hungarian (HU)

Additional languages can be added by updating `TRANSLATION_TARGET_LANGS` — no code changes needed.

---

## How Content Appears on the Live Site

- Static JSON translation files serve as defaults (instant page load).
- The app fetches text overrides from the database on page load.
- Database content takes priority over static files.
- If the backend is down, the site still works with the static defaults.
- Image overrides load from the database and replace CSS background images.

---

## System Messages

After actions, a status message appears below the content:

- **Green** — Success.
- **Red** — Error (persistent until resolved).
- **Yellow** — Warning (translation failed — retry or enter manually).

---

## Logging Out

Click the **Logout** button in the top-right toolbar.

---

## Tips

- Use **Preview & Edit** for the best experience — WYSIWYG editing.
- All text changes require all languages to be ready before saving.
- Image uploads show a preview before confirming — click Revert to undo.
- If DeepL is unavailable, the "Enter Manually" option appears after one retry.
- The admin panel works on mobile but desktop is recommended.
- Changes to text and images appear on the live site immediately (no deployment needed).
