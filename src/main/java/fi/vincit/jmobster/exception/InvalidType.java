package fi.vincit.jmobster.exception;/*
 * Copyright 2012 Juha Siponen
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

public class InvalidType extends RuntimeException {
    public InvalidType() {
    }

    public InvalidType(String s) {
        super(s);
    }

    public InvalidType(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidType(Throwable throwable) {
        super(throwable);
    }

}
