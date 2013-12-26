package ch.mjava.intellij.tapestry

import scala.Predef.String
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.WindowManager
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.awt.RelativePoint
import com.intellij.openapi.actionSystem.CommonDataKeys._

/**
 * Copyright 2013 http://www.mjava.ch

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 * @author knm
 */
class TapestryPartnerSwitcher extends AnAction {
  def actionPerformed(e: AnActionEvent): Unit = {
    //    val psiFile: PsiFile = e.getData(PSI_FILE)
    //
    //   getPartnerFiles(e) match {
    //     case Nil => showErrorBalloonWith(e, "No partnerFile found for " + psiFile.getName)
    //     case list =>  {
    //       val fileEditorManager: FileEditorManager = FileEditorManager.getInstance(e.getProject)
    //       for(f <- list) fileEditorManager.openFile(f.getVirtualFile, true, true)
    //     }
    //   }

  }

  //
  //  def getPartnerFiles(e: AnActionEvent): List[PsiFile] = {
  //    val psiFile: PsiFile = e.getData(PSI_FILE)
  //
  //    val name: String = psiFile.getName
  //    val partnerFile: String =
  //      if (name.endsWith("java"))
  //        name.replace(".java", ".html")
  //    else
  //        name.replace(".html", ".java")
  //
  //    val files = searchFiles(e, partnerFile)
  //
  //    if (files.isEmpty && name.endsWith("html")) {
  //      val jwcFileName: String = name.replace(".html", ".jwc")
  //      val psiFiles = searchFiles(e, jwcFileName)
  //      if (!psiFiles.isEmpty) {
  //        // TODO: parse those jwc-files and get those configured clases
  //        Nil
  //
  //      } else {
  //        Nil
  //      }
  //    }
  //    else
  //      files.filter(f => f.getParent.getName == psiFile.getParent.getName)
  //  }
  //
  //  private def searchFiles(e: AnActionEvent, fileName: String): List[PsiFile] = {
  //     val l = FilenameIndex.getFilesByName(AnAction.getEventProject(e), fileName, GlobalSearchScope.allScope(AnAction.getEventProject(e)))
  //
  //    for(f <- l) println(f.getName)
  //
  //    l.toList
  //  }
  //
  //  private def showErrorBalloonWith(e: AnActionEvent, message: String) {
  //    val statusBar: StatusBar = WindowManager.getInstance.getStatusBar(DataKeys.PROJECT.getData(e.getDataContext))
  //    JBPopupFactory.getInstance.createHtmlTextBalloonBuilder(message, MessageType.ERROR, null).setFadeoutTime(5000).createBalloon.show(RelativePoint.getCenterOf(statusBar.getComponent), Balloon.Position.atRight)
  //  }
}
