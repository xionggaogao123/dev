/* -*- Mode: Java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */
/* vim: set shiftwidth=2 tabstop=2 autoindent cindent expandtab: */
/* Copyright 2012 Mozilla Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 /* globals PDFJS, Util */

'use strict';

// List of shared files to include;
var sharedFiles = [
  '/static/js/exercise/plugins/util.js'
];

// List of other files to include;
var otherFiles = [
  '/static/js/exercise/plugins/core/network.js',
  '/static/js/exercise/plugins/core/chunked_stream.js',
  '/static/js/exercise/plugins/core/pdf_manager.js',
  '/static/js/exercise/plugins/core/core.js',
  '/static/js/exercise/plugins/core/obj.js',
  '/static/js/exercise/plugins/core/charsets.js',
  '/static/js/exercise/plugins/core/annotation.js',
  '/static/js/exercise/plugins/core/function.js',
  '/static/js/exercise/plugins/core/colorspace.js',
  '/static/js/exercise/plugins/core/crypto.js',
  '/static/js/exercise/plugins/core/pattern.js',
  '/static/js/exercise/plugins/core/evaluator.js',
  '/static/js/exercise/plugins/core/cmap.js',
  '/static/js/exercise/plugins/core/fonts.js',
  '/static/js/exercise/plugins/core/font_renderer.js',
  '/static/js/exercise/plugins/core/glyphlist.js',
  '/static/js/exercise/plugins/core/image.js',
  '/static/js/exercise/plugins/core/metrics.js',
  '/static/js/exercise/plugins/core/parser.js',
  '/static/js/exercise/plugins/core/ps_parser.js',
  '/static/js/exercise/plugins/core/stream.js',
  '/static/js/exercise/plugins/core/worker.js',
  '/static/js/exercise/plugins/core/arithmetic_decoder.js',
  '/static/js/exercise/plugins/core/jpg.js',
  '/static/js/exercise/plugins/core/jpx.js',
  '/static/js/exercise/plugins/core/jbig2.js',
  '/static/js/exercise/plugins/core/bidi.js',
  '/static/js/exercise/plugins/core/murmurhash3.js'
];

function loadInOrder(index, path, files) {
  if (index >= files.length) {
    PDFJS.fakeWorkerFilesLoadedCapability.resolve();
    return;
  }
  PDFJS.Util.loadScript(path + files[index],
                  loadInOrder.bind(null, ++index, path, files));
}

// Load all the files.
if (typeof PDFJS === 'undefined' || !PDFJS.fakeWorkerFilesLoadedCapability) {
  var files = sharedFiles.concat(otherFiles);
  for (var i = 0; i < files.length; i++) {
    importScripts(files[i]);
  }
} else {
  var src = PDFJS.workerSrc;
  var path = src.substr(0, src.indexOf('worker_loader.js'));
  // If Util is available, we assume that shared files are already loaded. Can
  // happen that they are not if PDF.js is bundled inside a special namespace.
  var skipShared = typeof Util !== 'undefined';
  var files = skipShared ? otherFiles : sharedFiles.concat(otherFiles);
  loadInOrder(0, path, files);
}
