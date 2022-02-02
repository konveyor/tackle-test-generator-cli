'''
This is a java class parser, download from here:
https://formats.kaitai.io/java_class/python.html
It was created by this tool:
https://kaitai.io/

'''
# This is a generated file! Please edit source .ksy file and use kaitai-struct-compiler to rebuild

from pkg_resources import parse_version
import kaitaistruct
from kaitaistruct import KaitaiStruct, KaitaiStream, BytesIO
from enum import Enum


if parse_version(kaitaistruct.__version__) < parse_version('0.9'):
    raise Exception("Incompatible Kaitai Struct Python API: 0.9 or later is required, but you have %s" % (kaitaistruct.__version__))

class JavaClass(KaitaiStruct):
    """
    .. seealso::
       Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1
    """
    def __init__(self, _io, _parent=None, _root=None):
        self._io = _io
        self._parent = _parent
        self._root = _root if _root else self
        self._read()

    def _read(self):
        self.magic = self._io.read_bytes(4)
        if not self.magic == b"\xCA\xFE\xBA\xBE":
            raise kaitaistruct.ValidationNotEqualError(b"\xCA\xFE\xBA\xBE", self.magic, self._io, u"/seq/0")
        self.version_minor = self._io.read_u2be()
        self.version_major = self._io.read_u2be()
        self.constant_pool_count = self._io.read_u2be()
        self.constant_pool = [None] * ((self.constant_pool_count - 1))
        for i in range((self.constant_pool_count - 1)):
            self.constant_pool[i] = JavaClass.ConstantPoolEntry((self.constant_pool[(i - 1)].is_two_entries if i != 0 else False), self._io, self, self._root)

        self.access_flags = self._io.read_u2be()
        self.this_class = self._io.read_u2be()
        self.super_class = self._io.read_u2be()
        self.interfaces_count = self._io.read_u2be()
        self.interfaces = [None] * (self.interfaces_count)
        for i in range(self.interfaces_count):
            self.interfaces[i] = self._io.read_u2be()

        self.fields_count = self._io.read_u2be()
        self.fields = [None] * (self.fields_count)
        for i in range(self.fields_count):
            self.fields[i] = JavaClass.FieldInfo(self._io, self, self._root)

        self.methods_count = self._io.read_u2be()
        self.methods = [None] * (self.methods_count)
        for i in range(self.methods_count):
            self.methods[i] = JavaClass.MethodInfo(self._io, self, self._root)

        self.attributes_count = self._io.read_u2be()
        self.attributes = [None] * (self.attributes_count)
        for i in range(self.attributes_count):
            self.attributes[i] = JavaClass.AttributeInfo(self._io, self, self._root)


    class FloatCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.5
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.value = self._io.read_f4be()


    class AttributeInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.name_index = self._io.read_u2be()
            self.attribute_length = self._io.read_u4be()
            _on = self.name_as_str
            if _on == u"SourceFile":
                self._raw_info = self._io.read_bytes(self.attribute_length)
                _io__raw_info = KaitaiStream(BytesIO(self._raw_info))
                self.info = JavaClass.AttributeInfo.AttrBodySourceFile(_io__raw_info, self, self._root)
            elif _on == u"LineNumberTable":
                self._raw_info = self._io.read_bytes(self.attribute_length)
                _io__raw_info = KaitaiStream(BytesIO(self._raw_info))
                self.info = JavaClass.AttributeInfo.AttrBodyLineNumberTable(_io__raw_info, self, self._root)
            elif _on == u"Exceptions":
                self._raw_info = self._io.read_bytes(self.attribute_length)
                _io__raw_info = KaitaiStream(BytesIO(self._raw_info))
                self.info = JavaClass.AttributeInfo.AttrBodyExceptions(_io__raw_info, self, self._root)
            elif _on == u"Code":
                self._raw_info = self._io.read_bytes(self.attribute_length)
                _io__raw_info = KaitaiStream(BytesIO(self._raw_info))
                self.info = JavaClass.AttributeInfo.AttrBodyCode(_io__raw_info, self, self._root)
            else:
                self.info = self._io.read_bytes(self.attribute_length)

        class AttrBodyCode(KaitaiStruct):
            """
            .. seealso::
               Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3
            """
            def __init__(self, _io, _parent=None, _root=None):
                self._io = _io
                self._parent = _parent
                self._root = _root if _root else self
                self._read()

            def _read(self):
                self.max_stack = self._io.read_u2be()
                self.max_locals = self._io.read_u2be()
                self.code_length = self._io.read_u4be()
                self.code = self._io.read_bytes(self.code_length)
                self.exception_table_length = self._io.read_u2be()
                self.exception_table = [None] * (self.exception_table_length)
                for i in range(self.exception_table_length):
                    self.exception_table[i] = JavaClass.AttributeInfo.AttrBodyCode.ExceptionEntry(self._io, self, self._root)

                self.attributes_count = self._io.read_u2be()
                self.attributes = [None] * (self.attributes_count)
                for i in range(self.attributes_count):
                    self.attributes[i] = JavaClass.AttributeInfo(self._io, self, self._root)


            class ExceptionEntry(KaitaiStruct):
                """
                .. seealso::
                   Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3
                """
                def __init__(self, _io, _parent=None, _root=None):
                    self._io = _io
                    self._parent = _parent
                    self._root = _root if _root else self
                    self._read()

                def _read(self):
                    self.start_pc = self._io.read_u2be()
                    self.end_pc = self._io.read_u2be()
                    self.handler_pc = self._io.read_u2be()
                    self.catch_type = self._io.read_u2be()

                @property
                def catch_exception(self):
                    if hasattr(self, '_m_catch_exception'):
                        return self._m_catch_exception if hasattr(self, '_m_catch_exception') else None

                    if self.catch_type != 0:
                        self._m_catch_exception = self._root.constant_pool[(self.catch_type - 1)]

                    return self._m_catch_exception if hasattr(self, '_m_catch_exception') else None



        class AttrBodyExceptions(KaitaiStruct):
            """
            .. seealso::
               Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.5
            """
            def __init__(self, _io, _parent=None, _root=None):
                self._io = _io
                self._parent = _parent
                self._root = _root if _root else self
                self._read()

            def _read(self):
                self.number_of_exceptions = self._io.read_u2be()
                self.exceptions = [None] * (self.number_of_exceptions)
                for i in range(self.number_of_exceptions):
                    self.exceptions[i] = JavaClass.AttributeInfo.AttrBodyExceptions.ExceptionTableEntry(self._io, self, self._root)


            class ExceptionTableEntry(KaitaiStruct):
                def __init__(self, _io, _parent=None, _root=None):
                    self._io = _io
                    self._parent = _parent
                    self._root = _root if _root else self
                    self._read()

                def _read(self):
                    self.index = self._io.read_u2be()

                @property
                def as_info(self):
                    if hasattr(self, '_m_as_info'):
                        return self._m_as_info if hasattr(self, '_m_as_info') else None

                    self._m_as_info = self._root.constant_pool[(self.index - 1)].cp_info
                    return self._m_as_info if hasattr(self, '_m_as_info') else None

                @property
                def name_as_str(self):
                    if hasattr(self, '_m_name_as_str'):
                        return self._m_name_as_str if hasattr(self, '_m_name_as_str') else None

                    self._m_name_as_str = self.as_info.name_as_str
                    return self._m_name_as_str if hasattr(self, '_m_name_as_str') else None



        class AttrBodySourceFile(KaitaiStruct):
            """
            .. seealso::
               Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.10
            """
            def __init__(self, _io, _parent=None, _root=None):
                self._io = _io
                self._parent = _parent
                self._root = _root if _root else self
                self._read()

            def _read(self):
                self.sourcefile_index = self._io.read_u2be()

            @property
            def sourcefile_as_str(self):
                if hasattr(self, '_m_sourcefile_as_str'):
                    return self._m_sourcefile_as_str if hasattr(self, '_m_sourcefile_as_str') else None

                self._m_sourcefile_as_str = self._root.constant_pool[(self.sourcefile_index - 1)].cp_info.value
                return self._m_sourcefile_as_str if hasattr(self, '_m_sourcefile_as_str') else None


        class AttrBodyLineNumberTable(KaitaiStruct):
            """
            .. seealso::
               Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.12
            """
            def __init__(self, _io, _parent=None, _root=None):
                self._io = _io
                self._parent = _parent
                self._root = _root if _root else self
                self._read()

            def _read(self):
                self.line_number_table_length = self._io.read_u2be()
                self.line_number_table = [None] * (self.line_number_table_length)
                for i in range(self.line_number_table_length):
                    self.line_number_table[i] = JavaClass.AttributeInfo.AttrBodyLineNumberTable.LineNumberTableEntry(self._io, self, self._root)


            class LineNumberTableEntry(KaitaiStruct):
                def __init__(self, _io, _parent=None, _root=None):
                    self._io = _io
                    self._parent = _parent
                    self._root = _root if _root else self
                    self._read()

                def _read(self):
                    self.start_pc = self._io.read_u2be()
                    self.line_number = self._io.read_u2be()



        @property
        def name_as_str(self):
            if hasattr(self, '_m_name_as_str'):
                return self._m_name_as_str if hasattr(self, '_m_name_as_str') else None

            self._m_name_as_str = self._root.constant_pool[(self.name_index - 1)].cp_info.value
            return self._m_name_as_str if hasattr(self, '_m_name_as_str') else None


    class MethodRefCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.class_index = self._io.read_u2be()
            self.name_and_type_index = self._io.read_u2be()

        @property
        def class_as_info(self):
            if hasattr(self, '_m_class_as_info'):
                return self._m_class_as_info if hasattr(self, '_m_class_as_info') else None

            self._m_class_as_info = self._root.constant_pool[(self.class_index - 1)].cp_info
            return self._m_class_as_info if hasattr(self, '_m_class_as_info') else None

        @property
        def name_and_type_as_info(self):
            if hasattr(self, '_m_name_and_type_as_info'):
                return self._m_name_and_type_as_info if hasattr(self, '_m_name_and_type_as_info') else None

            self._m_name_and_type_as_info = self._root.constant_pool[(self.name_and_type_index - 1)].cp_info
            return self._m_name_and_type_as_info if hasattr(self, '_m_name_and_type_as_info') else None


    class FieldInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.5
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.access_flags = self._io.read_u2be()
            self.name_index = self._io.read_u2be()
            self.descriptor_index = self._io.read_u2be()
            self.attributes_count = self._io.read_u2be()
            self.attributes = [None] * (self.attributes_count)
            for i in range(self.attributes_count):
                self.attributes[i] = JavaClass.AttributeInfo(self._io, self, self._root)


        @property
        def name_as_str(self):
            if hasattr(self, '_m_name_as_str'):
                return self._m_name_as_str if hasattr(self, '_m_name_as_str') else None

            self._m_name_as_str = self._root.constant_pool[(self.name_index - 1)].cp_info.value
            return self._m_name_as_str if hasattr(self, '_m_name_as_str') else None


    class DoubleCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.6
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.value = self._io.read_f8be()


    class LongCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.5
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.value = self._io.read_u8be()


    class InvokeDynamicCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.10
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.bootstrap_method_attr_index = self._io.read_u2be()
            self.name_and_type_index = self._io.read_u2be()


    class MethodHandleCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.8
        """

        class ReferenceKindEnum(Enum):
            get_field = 1
            get_static = 2
            put_field = 3
            put_static = 4
            invoke_virtual = 5
            invoke_static = 6
            invoke_special = 7
            new_invoke_special = 8
            invoke_interface = 9
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.reference_kind = KaitaiStream.resolve_enum(JavaClass.MethodHandleCpInfo.ReferenceKindEnum, self._io.read_u1())
            self.reference_index = self._io.read_u2be()


    class NameAndTypeCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.6
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.name_index = self._io.read_u2be()
            self.descriptor_index = self._io.read_u2be()

        @property
        def name_as_info(self):
            if hasattr(self, '_m_name_as_info'):
                return self._m_name_as_info if hasattr(self, '_m_name_as_info') else None

            self._m_name_as_info = self._root.constant_pool[(self.name_index - 1)].cp_info
            return self._m_name_as_info if hasattr(self, '_m_name_as_info') else None

        @property
        def name_as_str(self):
            if hasattr(self, '_m_name_as_str'):
                return self._m_name_as_str if hasattr(self, '_m_name_as_str') else None

            self._m_name_as_str = self.name_as_info.value
            return self._m_name_as_str if hasattr(self, '_m_name_as_str') else None

        @property
        def descriptor_as_info(self):
            if hasattr(self, '_m_descriptor_as_info'):
                return self._m_descriptor_as_info if hasattr(self, '_m_descriptor_as_info') else None

            self._m_descriptor_as_info = self._root.constant_pool[(self.descriptor_index - 1)].cp_info
            return self._m_descriptor_as_info if hasattr(self, '_m_descriptor_as_info') else None

        @property
        def descriptor_as_str(self):
            if hasattr(self, '_m_descriptor_as_str'):
                return self._m_descriptor_as_str if hasattr(self, '_m_descriptor_as_str') else None

            self._m_descriptor_as_str = self.descriptor_as_info.value
            return self._m_descriptor_as_str if hasattr(self, '_m_descriptor_as_str') else None


    class Utf8CpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.7
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.str_len = self._io.read_u2be()
            self.value = (self._io.read_bytes(self.str_len)).decode(u"UTF-8")


    class StringCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.3
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.string_index = self._io.read_u2be()


    class MethodTypeCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.9
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.descriptor_index = self._io.read_u2be()


    class InterfaceMethodRefCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.class_index = self._io.read_u2be()
            self.name_and_type_index = self._io.read_u2be()

        @property
        def class_as_info(self):
            if hasattr(self, '_m_class_as_info'):
                return self._m_class_as_info if hasattr(self, '_m_class_as_info') else None

            self._m_class_as_info = self._root.constant_pool[(self.class_index - 1)].cp_info
            return self._m_class_as_info if hasattr(self, '_m_class_as_info') else None

        @property
        def name_and_type_as_info(self):
            if hasattr(self, '_m_name_and_type_as_info'):
                return self._m_name_and_type_as_info if hasattr(self, '_m_name_and_type_as_info') else None

            self._m_name_and_type_as_info = self._root.constant_pool[(self.name_and_type_index - 1)].cp_info
            return self._m_name_and_type_as_info if hasattr(self, '_m_name_and_type_as_info') else None


    class ClassCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.1
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.name_index = self._io.read_u2be()

        @property
        def name_as_info(self):
            if hasattr(self, '_m_name_as_info'):
                return self._m_name_as_info if hasattr(self, '_m_name_as_info') else None

            self._m_name_as_info = self._root.constant_pool[(self.name_index - 1)].cp_info
            return self._m_name_as_info if hasattr(self, '_m_name_as_info') else None

        @property
        def name_as_str(self):
            if hasattr(self, '_m_name_as_str'):
                return self._m_name_as_str if hasattr(self, '_m_name_as_str') else None

            self._m_name_as_str = self.name_as_info.value
            return self._m_name_as_str if hasattr(self, '_m_name_as_str') else None


    class ConstantPoolEntry(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4
        """
        tag = ''
        class TagEnum(Enum):
            utf8 = 1
            integer = 3
            float = 4
            long = 5
            double = 6
            class_type = 7
            string = 8
            field_ref = 9
            method_ref = 10
            interface_method_ref = 11
            name_and_type = 12
            method_handle = 15
            method_type = 16
            invoke_dynamic = 18
        def __init__(self, is_prev_two_entries, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self.is_prev_two_entries = is_prev_two_entries
            self._read()

        def _read(self):
            if not (self.is_prev_two_entries):
                self.tag = KaitaiStream.resolve_enum(JavaClass.ConstantPoolEntry.TagEnum, self._io.read_u1())

            if not (self.is_prev_two_entries):
                _on = self.tag
                if _on == JavaClass.ConstantPoolEntry.TagEnum.interface_method_ref:
                    self.cp_info = JavaClass.InterfaceMethodRefCpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.class_type:
                    self.cp_info = JavaClass.ClassCpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.utf8:
                    self.cp_info = JavaClass.Utf8CpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.method_type:
                    self.cp_info = JavaClass.MethodTypeCpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.integer:
                    self.cp_info = JavaClass.IntegerCpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.string:
                    self.cp_info = JavaClass.StringCpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.float:
                    self.cp_info = JavaClass.FloatCpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.long:
                    self.cp_info = JavaClass.LongCpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.method_ref:
                    self.cp_info = JavaClass.MethodRefCpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.double:
                    self.cp_info = JavaClass.DoubleCpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.invoke_dynamic:
                    self.cp_info = JavaClass.InvokeDynamicCpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.field_ref:
                    self.cp_info = JavaClass.FieldRefCpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.method_handle:
                    self.cp_info = JavaClass.MethodHandleCpInfo(self._io, self, self._root)
                elif _on == JavaClass.ConstantPoolEntry.TagEnum.name_and_type:
                    self.cp_info = JavaClass.NameAndTypeCpInfo(self._io, self, self._root)


        @property
        def is_two_entries(self):
            if hasattr(self, '_m_is_two_entries'):
                return self._m_is_two_entries if hasattr(self, '_m_is_two_entries') else None

            self._m_is_two_entries =  ((self.tag == JavaClass.ConstantPoolEntry.TagEnum.long) or (self.tag == JavaClass.ConstantPoolEntry.TagEnum.double)) 
            return self._m_is_two_entries if hasattr(self, '_m_is_two_entries') else None


    class MethodInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.6
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.access_flags = self._io.read_u2be()
            self.name_index = self._io.read_u2be()
            self.descriptor_index = self._io.read_u2be()
            self.attributes_count = self._io.read_u2be()
            self.attributes = [None] * (self.attributes_count)
            for i in range(self.attributes_count):
                self.attributes[i] = JavaClass.AttributeInfo(self._io, self, self._root)


        @property
        def name_as_str(self):
            if hasattr(self, '_m_name_as_str'):
                return self._m_name_as_str if hasattr(self, '_m_name_as_str') else None

            self._m_name_as_str = self._root.constant_pool[(self.name_index - 1)].cp_info.value
            return self._m_name_as_str if hasattr(self, '_m_name_as_str') else None


    class IntegerCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.4
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.value = self._io.read_u4be()


    class FieldRefCpInfo(KaitaiStruct):
        """
        .. seealso::
           Source - https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2
        """
        def __init__(self, _io, _parent=None, _root=None):
            self._io = _io
            self._parent = _parent
            self._root = _root if _root else self
            self._read()

        def _read(self):
            self.class_index = self._io.read_u2be()
            self.name_and_type_index = self._io.read_u2be()

        @property
        def class_as_info(self):
            if hasattr(self, '_m_class_as_info'):
                return self._m_class_as_info if hasattr(self, '_m_class_as_info') else None

            self._m_class_as_info = self._root.constant_pool[(self.class_index - 1)].cp_info
            return self._m_class_as_info if hasattr(self, '_m_class_as_info') else None

        @property
        def name_and_type_as_info(self):
            if hasattr(self, '_m_name_and_type_as_info'):
                return self._m_name_and_type_as_info if hasattr(self, '_m_name_and_type_as_info') else None

            self._m_name_and_type_as_info = self._root.constant_pool[(self.name_and_type_index - 1)].cp_info
            return self._m_name_and_type_as_info if hasattr(self, '_m_name_and_type_as_info') else None



